
package net.mlpnn.controller;

import net.mlpnn.enums.DataSetInfo;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author Lindes Roets
 */
public class BaseController {
	
	@ModelAttribute("datasets")
	public DataSetInfo[] addDataSets(){
		return DataSetInfo.values();
	}

}
