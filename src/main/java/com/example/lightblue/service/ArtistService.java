package com.example.lightblue.service;

import com.example.lightblue.dto.ArtistCreateRequest;
import com.example.lightblue.dto.ArtistUpdateRequest;
import com.example.lightblue.model.Account;
import com.example.lightblue.model.Artist;
import com.example.lightblue.model.Portfolio;
import com.example.lightblue.model.PortfolioFile;
import com.example.lightblue.repository.AccountRepository;
import com.example.lightblue.repository.ArtistRepository;
import com.example.lightblue.repository.PortfolioRepository;
import com.example.lightblue.repository.PortfolioFileRepository;
import com.example.lightblue.dto.PortfolioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.lightblue.model.enums.City;
import com.example.lightblue.model.enums.ProjectType;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ArtistService {

    @Autowired
    private Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioFileRepository portfolioFileRepository;

    public Page<Artist> searchArtists(City city, Integer career, Boolean hasPortfolios, Pageable pageable) {
        return artistRepository.findAll((Specification<Artist>) (root, query, cb) -> {
            Predicate finalPredicate = cb.conjunction(); // Start with a true predicate

            if (city != null) { // No need for isEmpty() check for enum
                finalPredicate = cb.and(finalPredicate, cb.equal(root.get("city"), city));
            }

            if (career != null) {
                finalPredicate = cb.and(finalPredicate, cb.greaterThanOrEqualTo(root.get("career"), career));
            }

            if (hasPortfolios != null) {
                if (hasPortfolios) {
                    finalPredicate = cb.and(finalPredicate, cb.isNotEmpty(root.get("portfolios")));
                } else {
                    finalPredicate = cb.and(finalPredicate, cb.isEmpty(root.get("portfolios")));
                }
            }

            return finalPredicate;
        }, pageable);
    }

    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }

    @Transactional
    public Artist createArtist(ArtistCreateRequest artistDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated.");
        }
        Long accountId = ((Account) authentication.getPrincipal()).getId();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id " + accountId));

        // Check if an Artist already exists for this Account
        if (account.getArtist() != null) {
            throw new RuntimeException("Artist already exists for this account.");
        }

        // Set accountType to ARTIST
        account.setAccountType("ARTIST");
        accountRepository.save(account);

        Artist artist = new Artist();
        artist.setAccount(account);
        artist.setName(artistDetails.getName());
        artist.setPhone(artistDetails.getPhone());
        artist.setEmail(artistDetails.getEmail());
        artist.setCareer(artistDetails.getCareer());
        artist.setJobField(artistDetails.getJobField());
        artist.setCity(artistDetails.getCity());
        artist.setActivityField(artistDetails.getActivityField());
        artist.setDesiredCollaborationField(artistDetails.getDesiredCollaborationField());
        artist.setIntroduction(artistDetails.getIntroduction());
        return artistRepository.save(artist);
    }

    @Transactional
    public Artist updateArtist(Long id, ArtistUpdateRequest artistDetails) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id " + id));
        artist.setName(artistDetails.getName());
        artist.setPhone(artistDetails.getPhone());
        artist.setEmail(artistDetails.getEmail());
        artist.setCareer(artistDetails.getCareer());
        artist.setJobField(artistDetails.getJobField());
        artist.setCity(artistDetails.getCity());
        artist.setActivityField(artistDetails.getActivityField());
        artist.setDesiredCollaborationField(artistDetails.getDesiredCollaborationField());
        artist.setIntroduction(artistDetails.getIntroduction());
        return artistRepository.save(artist);
    }

    public void deleteArtist(Long id) {
        artistRepository.deleteById(id);
    }

    // Portfolio related methods
    public List<Portfolio> getPortfoliosByArtistId(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found with id " + artistId));
        return artist.getPortfolios(); // This requires a OneToMany mapping in Artist model
    }

    @Transactional
    public Portfolio addPortfolioToArtist(Long artistId, PortfolioRequest portfolioRequest, List<MultipartFile> files) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found with id " + artistId));

        Portfolio portfolio = new Portfolio();
        portfolio.setUrl(portfolioRequest.getUrl());
        portfolio.setArtist(artist);

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    String fileUri = saveFile(file);
                    PortfolioFile portfolioFile = new PortfolioFile();
                    portfolioFile.setFileUri(fileUri);
                    portfolio.addFile(portfolioFile);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file", e);
                }
            }
        }

        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Portfolio updatePortfolio(Long portfolioId, PortfolioRequest portfolioRequest, List<MultipartFile> files) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with id " + portfolioId));

        portfolio.setUrl(portfolioRequest.getUrl());

        // Clear existing files and add new ones
        // Delete old files from GCS if they exist
        portfolio.getFiles().forEach(file -> {
            String fileName = file.getFileUri().substring(file.getFileUri().lastIndexOf('/') + 1);
            storage.delete(bucketName, fileName);
        });
        portfolio.getFiles().clear();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    String fileUri = saveFile(file);
                    PortfolioFile portfolioFile = new PortfolioFile();
                    portfolioFile.setFileUri(fileUri);
                    portfolio.addFile(portfolioFile);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file", e);
                }            }
        }

        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with id " + portfolioId));

        // Delete associated files from GCS
        portfolio.getFiles().forEach(file -> {
            String fileName = file.getFileUri().substring(file.getFileUri().lastIndexOf('/') + 1);
            storage.delete(bucketName, fileName);
        });

        portfolioRepository.deleteById(portfolioId);
    }

    private String saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
            fileExtension = originalFilename.substring(dotIndex);
        }

        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        BlobId blobId = BlobId.of(bucketName, uniqueFilename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        storage.create(blobInfo, file.getBytes());

        // Return the public URL of the uploaded file
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, uniqueFilename);
    }
}
