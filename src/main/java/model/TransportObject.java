package model;

/**
 * Objekt zum Serialisieren zwischen Client/Server
 * @param <O>
 */
public class TransportObject<O>  {
  public TransportObjectType type;
  public O object;
  public String token;

  public TransportObject(TransportObjectType type, O obj) {
        this.type = type;
        this.object = obj;
        this.token = "";
  }

  public TransportObject(TransportObjectType type, O obj, String token) {
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
