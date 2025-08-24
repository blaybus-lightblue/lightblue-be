package com.example.lightblue.dto;

import com.example.lightblue.model.Portfolio;
import com.example.lightblue.model.PortfolioFile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PortfolioDTO {

    private Long id;
    private Long artistId;
    private List<PortfolioFileDTO> files;

    public PortfolioDTO(Portfolio portfolio) {
        this.id = portfolio.getId();
        this.artistId = portfolio.getArtist().getId();
        this.files = portfolio.getFiles().stream()
                .map(PortfolioFileDTO::new)
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class PortfolioFileDTO {
        private Long id;
        @Schema(description = "업로드된 파일의 접근 URL")
        private String fileUri;

        public PortfolioFileDTO(PortfolioFile file) {
            this.id = file.getId();
            this.fileUri = file.getFileUri();
        }
    }
}
