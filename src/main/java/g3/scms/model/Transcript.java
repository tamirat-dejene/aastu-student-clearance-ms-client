package g3.scms.model;

// Corresponds to the re request body Transcript

// We use gson to convert the object into json

public class Transcript {
  private String audio_url;
  private String id;
  private String text;
  private String status;

  public String getAudio_url() {
    return audio_url;
  }

  public void setAudio_url(String audio_url) {
    this.audio_url = audio_url;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  
}
