package com.themoviedb.apis.entity.responses;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Generated("com.robohorse.robopojogenerator")
public class Videos{

	@SerializedName("videos")
	private Videos videos;

	@SerializedName("results")
	private List<ResultsItem> results;

	public void setVideos(Videos videos){
		this.videos = videos;
	}

	public Videos getVideos(){
		return videos;
	}

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"Videos{" + 
			"videos = '" + videos + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}