package com.ghost.recipewebapp.dto;

import lombok.*;

import javax.validation.constraints.Min;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Uses to represent recipe in search
// Not saves in database
@Setter
@Getter
@NoArgsConstructor
public class RecipeSearch {
    public static final int DEFAULT_PAGE_NUMBER = 1;
    public static final int DEFAULT_PAGE_SIZE = 1;

    private String title;
    private Boolean image;
    @Min(1)
    private Integer time;
    @Min(1)
    private Integer calories;
    private List<String> products = new ArrayList<>();
    @Min(1)
    private Integer currentPage;
    @Min(1)
    private Integer pageSize;

    private Boolean inFavourites;

    public Integer getCurrentPageOrDefault() {
        return Optional.ofNullable(currentPage).orElse(RecipeSearch.DEFAULT_PAGE_NUMBER);
    }

    public Integer getPageSizeOrDefault() {
        return Optional.ofNullable(pageSize).orElse(RecipeSearch.DEFAULT_PAGE_SIZE);
    }

    // return title, image, time, calories, products as url params
    public String getFilterParams() {
        List<String> params = new ArrayList<>();

        if (Objects.nonNull(title)) {
            params.add("title=" + URLEncoder.encode(title, StandardCharsets.UTF_8));
        }

        if (Objects.nonNull(image) && image) {
            params.add("image=on");
        }

        if (Objects.nonNull(time)) {
            params.add("time=" + time);
        }

        if (Objects.nonNull(calories)) {
            params.add("calories=" + calories);
        }

        for (int i = 0; i < products.size(); i++) {
            String productParam = "products" +
                    URLEncoder.encode("[", StandardCharsets.UTF_8) +
                    i +
                    URLEncoder.encode("]", StandardCharsets.UTF_8) +
                    "=" +
                    URLEncoder.encode(products.get(i), StandardCharsets.UTF_8);

            params.add(productParam);
        }

        if (Objects.nonNull(inFavourites) && inFavourites) {
            params.add("inFavourites=on");
        }

        String result = String.join("&", params);

        if (result.equals("")) {
            return null;
        }

        return result;
    }

    // return currentPage as url param
    public String getCurrentPageParam() {
        return Objects.nonNull(currentPage) ? "currentPage=" + currentPage : null;
    }

    // return pageSize as url param
    public String getPageSizeParam() {
        return Objects.nonNull(pageSize) ? "pageSize=" + pageSize : null;
    }
}
