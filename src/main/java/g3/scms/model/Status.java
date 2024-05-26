package g3.scms.model;

public enum Status {
  PENDING,
  APPROVED,
  DECLINED;

  public static Status fromString(String status) {
    return status.equals("PENDING") ? Status.PENDING : status.equals("APPROVED") ? Status.APPROVED : Status.DECLINED;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
