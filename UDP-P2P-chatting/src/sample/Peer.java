package sample;

public class Peer {
    private String peerName;
    private int portNumber;
    private String ipNumber;

    public Peer() {

    }

    public Peer(String peerName, int portNumber, String ipNumber) {
        this.peerName = peerName;
        this.portNumber = portNumber;
        this.ipNumber = ipNumber;
    }

    public String getPeerName() {
        return peerName;
    }

    public void setPeerName(String peerName) {
        this.peerName = peerName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getIpNumber() {
        return ipNumber;
    }

    public void setIpNumber(String ipNumber) {
        this.ipNumber = ipNumber;
    }
}
