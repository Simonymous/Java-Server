package model;

public class TransportObject<O>  {
  public TramsportObjectType type;
  public O object;
  public String token;

    public TransportObject(TramsportObjectType type, O obj) {
        this.type = type;
        this.object = obj;
        this.token = "";
    }
  public TransportObject(TramsportObjectType type, O obj, String token) {
      this.type = type;
      this.object = obj;
      this.token = token;
  }

    @Override
    public String toString() {
        return "TransportObject{" +
                "type=" + type +
                ", object=" + object +
                ", token=" + token +
                '}';
    }

}
