package com.example.melotunes;

public class UploadSong {
    private String songName,SongUrl;
    public UploadSong(){
    }
    public UploadSong(String songName,String SongUrl)
    {
        this.songName=songName;
        this.SongUrl=SongUrl;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }
}
