package model;

import java.awt.*;

public class BaseBlob {
    private Blob blob;
    public BaseBlob(Point blobCenterPoint, int radius) {
        blob = new Blob(blobCenterPoint, radius);
    }
    public Blob getBlob() {
        return blob;
    }
}