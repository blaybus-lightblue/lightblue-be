package com.example.lightblue.service;

import com.example.lightblue.model.Artist;
import com.example.lightblue.model.Portfolio;
import com.example.lightblue.model.PortfolioFile;
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

@Service
public class ArtistService {

    private final String UPLOAD_DIR = "uploads";

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioFileRepository portfolioFileRepository;

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }

    @Transactional
    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    @Transactional
    public Artist updateArtist(Long id, Artist artistDetails) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id " + id));
        artist.setName(artistDetails.getName());
        artist.setAccount(artistDetails.getAccount()); // Assuming account can be updated or is set correctly
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
        portfolioRepository.deleteById(portfolioId);
    }

    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
            fileExtension = originalFilename.substring(dotIndex);
        }

        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);

        return "/" + UPLOAD_DIR + "/" + uniqueFilename;
    }
}
