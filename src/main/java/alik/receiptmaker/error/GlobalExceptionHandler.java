package alik.receiptmaker.error;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO("not-found", ex.getMessage());
        return ResponseEntity.status(404).body(errorDTO);
    }

}
