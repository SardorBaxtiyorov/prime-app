package org.exp.primeapp.controller.attachment;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exp.primeapp.service.interfaces.attachment.AttachmentService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.exp.primeapp.utils.Const.*;

@Slf4j
@RestController
@MultipartConfig
@RequiredArgsConstructor
@RequestMapping(API + V1 + ATTACHMENT)
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/{attachmentId}")
    public void getAttachment(@PathVariable Long attachmentId, HttpServletResponse response) throws IOException {
        log.debug("Fetching attachment with ID: {}", attachmentId);
        attachmentService.get(attachmentId, response);
        log.info("Successfully served attachment: {}", attachmentId);
    }
}
