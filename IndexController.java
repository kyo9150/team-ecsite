package jp.co.internous.kabuki.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.internous.kabuki.model.domain.MstCategory;
import jp.co.internous.kabuki.model.domain.MstProduct;
import jp.co.internous.kabuki.model.form.SearchForm;
import jp.co.internous.kabuki.model.mapper.MstCategoryMapper;
import jp.co.internous.kabuki.model.mapper.MstProductMapper;
import jp.co.internous.kabuki.model.session.LoginSession;




@Controller
@RequestMapping("/kabuki")
public class IndexController {
	
	@Autowired 
	private MstCategoryMapper categoryMapper;
	
	@Autowired
	private MstProductMapper productMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	
	@RequestMapping("/")
	public String index(Model m) {
		
		if(!loginSession.getIsLogin() && loginSession.getTemporaryUserId() == 0) {
			int tmpUserId = (int)(Math.random() * 1000000000 * -1);
			while (tmpUserId > -100000000) {
				tmpUserId *= 10;
			}
			loginSession.setTemporaryUserId(tmpUserId);
		}
	
		
		List<MstCategory> categories = categoryMapper.find();
		
		List<MstProduct> products = productMapper.find();
		
		m.addAttribute("categories", categories);
		m.addAttribute("selected", 0);
		m.addAttribute("products", products);
		m.addAttribute("loginSession", loginSession);

		return "index";
	}

	@RequestMapping("/searchItem")
	public String inex(SearchForm f,Model m) {
		List<MstProduct> products = null;
		
		String keywords = f.getKeywords().replaceAll("　", " ").replaceAll("\\s{2,}", " ").trim();
		if(f.getCategory() == 0) {

			products = productMapper.findByProductName(keywords.split(" "));
		} else {
			products = productMapper.findByCategoryAndProductName(f.getCategory(), keywords.split(" "));
		}
			
		List<MstCategory> categories = categoryMapper.find();
		m.addAttribute("keywords", keywords);
		m.addAttribute("selected", f.getCategory());
		m.addAttribute("categories", categories);
		m.addAttribute("products", products);
		m.addAttribute("loginSession", loginSession);

		
		return "index";
	
	}
}