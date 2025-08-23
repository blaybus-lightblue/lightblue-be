package com.example.lightblue.dto;

import com.example.lightblue.model.Portfolio;
import com.example.lightblue.model.PortfolioFile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PortfolioDTO {

    private Long id;
    private Long artistId;
    private String url;
    private List<PortfolioFileDTO> files;

    public PortfolioDTO(Portfolio portfolio) {
        this.id = portfolio.getId();
        this.artistId = portfolio.getArtist().getId();
        this.url = portfolio.getUrl();
        this.files = portfolio.getFiles().stream()
                .map(PortfolioFileDTO::new)
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class PortfolioFileDTO {
        private Long id;
        private String fileUri;

        public PortfolioFileDTO(PortfolioFile file) {
            this.id = file.getId();
            this.fileUri = file.getFileUri();
        }
    }
}
