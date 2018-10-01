package edu.osu.cse5234.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.osu.cse5234.model.*;


@Controller
@RequestMapping("/purchase")
public class Purchase {

	@RequestMapping(method = RequestMethod.GET)
	public String viewOrderEntryForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<Item> itemList = new ArrayList<>();
		Order order = new Order();
		String name = "";
		String price = "10";
		String[] laptops = {"", "MacBook", "Lenovo", "HP", "Dell", "Thinkpad"};


		for (int i = 1; i < 6; i++) {
			Item item = new Item();
			item.setName(name + laptops[i]);
			item.setPrice(price + (i*4));
			item.setQuantity("1");
			itemList.add(item);
		}
		order.setItems(itemList);
		order.setOrderNumber(itemList.size() + "2018");
		request.setAttribute("order", order);
		return "OrderEntryForm";
	}


	@RequestMapping(path = "/submitItems", method = RequestMethod.POST)
	public String submitItems(@ModelAttribute("order") Order order, HttpServletRequest request) {
		request.getSession().setAttribute("order", order);
		return "redirect:/purchase/paymentEntry";
	}

	@RequestMapping(path = "/paymentEntry", method = RequestMethod.GET)
	public String viewPaymentEntryPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Order order = (Order)request.getSession().getAttribute("order");
		PaymentInfo payment = new PaymentInfo();
		payment.setHolderName("Holder");
		payment.setccNumber("123456789");
		payment.setcvvCode("110");
		payment.setexpDate("9/22");
		request.setAttribute("payment", payment);
		
		return "PaymentEntryForm";
	}

	@RequestMapping(path = "/submitPayment", method = RequestMethod.POST)
	public String submitPayment(@ModelAttribute("payment") PaymentInfo payment, HttpServletRequest request) {
		request.getSession().setAttribute("payment", payment);
		return "redirect:/purchase/shippingEntry";
	}

	@RequestMapping(path = "/shippingEntry", method = RequestMethod.GET)
	public String viewShippingEntryPage(HttpServletRequest request, HttpServletResponse response) {
		ShippingInfo shipping = new ShippingInfo();

		shipping.setName("AddressLiver");
		shipping.setAddLine1("addline1");
		shipping.setAddLine2("addline2");
		shipping.setCity("Columbus");
		shipping.setState("Ohio");
		shipping.setZip("43202");
		request.setAttribute("shipping", shipping);
		return "ShippingEntryForm";
	}
	
	@RequestMapping(path = "/submitShipping", method = RequestMethod.POST)
	public String submitShipping(@ModelAttribute("shippingInfo") ShippingInfo shippingInfo,
			HttpServletRequest request) {
		request.getSession().setAttribute("shippingInfo", shippingInfo);
		return "redirect:/purchase/viewOrder";
	}

	@RequestMapping(path = "/viewOrder", method = RequestMethod.GET)
	public String viewOrder(HttpServletRequest request, HttpServletResponse response) {
		Order order = (Order)request.getSession().getAttribute("order");
		request.setAttribute("vieworder", order);
		return "ViewOrder";
	}

	@RequestMapping(path = "/confirmOrder", method = RequestMethod.POST)
	public String confirmOrder(@ModelAttribute("order") Order order, HttpServletRequest request) {
		order.confirm();
		return "redirect:/purchase/viewConfirmation";
	}

	@RequestMapping(path = "/viewConfirmation", method = RequestMethod.GET)
	public String viewConfirmation(HttpServletRequest request, HttpServletResponse response) {
		// display order detail.
		request.setAttribute("confirmedorder", request.getSession().getAttribute("order"));
		request.setAttribute("payment", request.getSession().getAttribute("payment"));	
		request.setAttribute("shipping", request.getSession().getAttribute("shippingInfo"));	
		return "Confirmation";
	}
}
