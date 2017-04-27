package com.abohomol.gifky.repository.retrofit;

class GifResponse {

    private GifResponseItem[] data;

    public GifResponseItem[] getData() {
        return data;
    }

    static class GifResponseItem {

        private String source_tld;
        private ImageList images;

        public String getSource() {
            return source_tld;
        }

        public String getUrl() {
            return images.fixed_height.url;
        }
    }

    private static class ImageList {
        private Gif fixed_height;
    }

    private static class Gif {
        private String url;
    }
}
