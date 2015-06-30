package model;

import java.awt.*;


public class BaseBlob {
    private Blob blob;
    //TODO: Make this class contain 1 blob instead of being a blob.
    public BaseBlob(Point blobCenterPoint, int radius) {
        blob = new Blob(blobCenterPoint, radius);
    }

    public Blob getBlob() {
        return blob;
    }
}