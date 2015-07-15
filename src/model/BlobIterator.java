package model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Adam on 2015-07-14.
 */
public class BlobIterator implements Iterator<Blob> {
    private ArrayList<AdvBlob.Cluster> clusters;
    private int blobIndex;
    private int clusterIndex;

    public BlobIterator(ArrayList<AdvBlob.Cluster> clusters) {
        this.clusters = clusters;
        //Initiated to -1 since "next" method will provide blobIndex+1 = 0.
        blobIndex = -1;
        clusterIndex = 0;
    }

    @Override
    public boolean hasNext() {
        if (clusters.isEmpty()) return false;
        if(clusters.get(clusterIndex).getSize() > blobIndex+1 || clusters.size() > clusterIndex+1) return true;
        return false;
    }

    @Override
    public Blob next() {
        if (!hasNext()) return null;
        if (clusters.get(clusterIndex).getSize() > (blobIndex+1)) {
            blobIndex++;
        }else {
            clusterIndex++;
            blobIndex = 0;
        }
        return clusters.get(clusterIndex).get(blobIndex);
    }

    @Override
    public void remove() {
        if (clusters.isEmpty()) return;
        clusters.get(clusterIndex).remove(clusters.get(clusterIndex).get(blobIndex));
        blobIndex--;
        if (blobIndex < 0) {
            blobIndex = 0;
            clusterIndex--;
            if (clusterIndex < 0) {
                clusters.remove(0);
            }
        }
    }
}
