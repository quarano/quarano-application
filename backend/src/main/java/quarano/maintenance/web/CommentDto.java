package quarano.maintenance.web;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto extends QuaranoDto {
    private LocalDateTime date;
    private String text, author;
}
