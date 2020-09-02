package model;

public class TransportObject<O>  {
  public TramsportObjectType type;
  public O object;

  public TransportObject(TramsportObjectType type, O obj) {
      this.type = type;
      this.object = obj;
  }

    @Override
    public String toString() {
        return "TransportObject{" +
                "type=" + type +
                ", object=" + object +
                '}';
    }

}
