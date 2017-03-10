package edu.csulb.android.photonote;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Samruddhi on 3/6/2017.
 */

public class PhotoNote {
    private String caption;
    private String filePath;
    private String created_at;

    public PhotoNote(String caption, String filePath, String created_at){
        this.caption = caption;
        this.filePath = filePath;
        this.created_at = created_at;
    }

    public String getCaption(){
        return this.caption;
    }

    public String getFilePath() throws IOException{
        return this.filePath;
    }

    public String getCreatedAt(){
        return this.created_at;
    }
}
