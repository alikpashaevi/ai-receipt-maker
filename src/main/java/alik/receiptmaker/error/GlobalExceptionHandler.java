package alik.receiptmaker.error;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("invalid-request", errorMessage));
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO("not-found", ex.getMessage());
        return ResponseEntity.status(404).body(errorDTO);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorDTO> handleInvalidLoginException(InvalidLoginException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDTO("invalid-login", exception.getMessage()));
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorDTO> handleUserExistsException(UserExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDTO("username-exists", exception.getMessage()));
    }

}
