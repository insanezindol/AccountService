package kr.co.lunasoft.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.co.lunasoft.vo.ResponseInfo;

@FeignClient(name = "customer-service")
public interface CustomerClient {

	@GetMapping("/customer/{path}")
	ResponseInfo callCustomer(@PathVariable("path") String path);

}
