package org.psoft.ecommerce.service.client.data;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CategoryPathClientView {

	List<PathElevemtClientView> path = new ArrayList<>();

	@Data
	public static class PathElevemtClientView {
		Long id;
	
		String name;
	}
}