package com.jagex.awt;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public final class ImageLoader {

    private final Toolkit toolkit;

    public ImageLoader(Toolkit toolkit) {
        this.toolkit = toolkit;
    }

    public List<Image> loadAll(List<String> resources) {
        return resources.stream()
            .map(this::load)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @param resource an absolute classpath name, such as {@code /icon16.png}
     */
    public Image load(String resource) {
        return toolkit.getImage(urlOf(resource));
    }

    private URL urlOf(String resource) {
        var url = getClass().getResource(resource);

        if (url == null) {
            throw new IllegalArgumentException("No such resource: " + resource);
        } else {
            return url;
        }
    }
}
