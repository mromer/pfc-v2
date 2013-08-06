package com.mromer.bikeclimber.task;

import java.util.List;

import com.mromer.bikeclimber.bean.ElevationSearchResponse;

public interface GetRoutesTaskResultI {
	
	public void taskSuccess(List<ElevationSearchResponse> result);
	
	public void taskFailure(String error);

}
