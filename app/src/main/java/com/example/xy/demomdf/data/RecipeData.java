package com.example.xy.demomdf.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RecipeData implements Parcelable {

    private int recipeIndex;
    private String recipeName;
    private int servings;
    private String imageUrl;

    public ArrayList<RecipeData.RecipeIngredient> recipeIngredientArrayList;
    public ArrayList<RecipeData.RecipeStep> recipeStepArrayList;

    public RecipeData(Parcel in) {
        recipeIndex = in.readInt();
        recipeName = in.readString();
        servings = in.readInt();
        imageUrl = in.readString();

    }

    public static final Creator<RecipeData> CREATOR = new Creator<RecipeData>() {
        @Override
        public RecipeData createFromParcel(Parcel in) {
            return new RecipeData(in);
        }

        @Override
        public RecipeData[] newArray(int size) {
            return new RecipeData[size];
        }
    };

    public int getRecipeIndex() {
        return recipeIndex;
    }

    public void setRecipeIndex(int recipeIndex) {
        this.recipeIndex = recipeIndex;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipeIndex);
        dest.writeString(recipeName);
        dest.writeInt(servings);
        dest.writeString(imageUrl);
    }

    public ArrayList<RecipeIngredient> getRecipeIngredientArrayList() {
        return recipeIngredientArrayList;
    }

    public void setRecipeIngredientArrayList(ArrayList<RecipeIngredient> recipeIngredientArrayList) {
        this.recipeIngredientArrayList = recipeIngredientArrayList;
    }

    public ArrayList<RecipeStep> getRecipeStepArrayList() {
        return recipeStepArrayList;
    }

    public void setRecipeStepArrayList(ArrayList<RecipeStep> recipeStepArrayList) {
        this.recipeStepArrayList = recipeStepArrayList;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static class RecipeIngredient implements Parcelable {

        private double quantity;
        private String measure;
        private String ingredient;

        public RecipeIngredient(Parcel in) {
            quantity = in.readDouble();
            measure = in.readString();
            ingredient = in.readString();
        }

        public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
            @Override
            public RecipeIngredient createFromParcel(Parcel in) {
                return new RecipeIngredient(in);
            }

            @Override
            public RecipeIngredient[] newArray(int size) {
                return new RecipeIngredient[size];
            }
        };

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(quantity);
            dest.writeString(measure);
            dest.writeString(ingredient);
        }
    }

    public static class RecipeStep implements Parcelable {

        private int id;
        private String shortDescription;
        private String mainDescription;
        private String videoUrl;
        private String thumbnailUrl;

        public RecipeStep(Parcel in) {
            id = in.readInt();
            shortDescription = in.readString();
            mainDescription = in.readString();
            videoUrl = in.readString();
            thumbnailUrl = in.readString();
        }

        public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>() {
            @Override
            public RecipeStep createFromParcel(Parcel in) {
                return new RecipeStep(in);
            }

            @Override
            public RecipeStep[] newArray(int size) {
                return new RecipeStep[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getMainDescription() {
            return mainDescription;
        }

        public void setMainDescription(String mainDescription) {
            this.mainDescription = mainDescription;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(shortDescription);
            dest.writeString(mainDescription);
            dest.writeString(videoUrl);
            dest.writeString(thumbnailUrl);
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }
    }
}
