package quarano.core.web;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.TransformedResource;

/**
 * @author Oliver Drotbohm
 */
@Profile("standalone")
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
class QuaranoStandaloneWebConfiguration implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/")
				.resourceChain(true)
				.addResolver(new PathResourceResolver() {
					/* We have to help of our NG Frontend Router of our SPA!
					 * The concrete path resolution is done in our Frontend APP.
					 *
					 * In webserver speech: we're rewriting the paths of the files served
					 * if the file of the requested path exists, we're serving it
					 * if not, we serve the NG index.html.
					 */
					@Override
					protected Resource getResource(String resourcePath, Resource location) throws IOException {
						Resource requestedResource = location.createRelative(resourcePath);

						return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
								: new ClassPathResource("/static/index.html");
					}
				})
				.addTransformer((httpServletRequest, resource, resourceTransformerChain) -> {
					if (resource instanceof ClassPathResource
							&& resource.exists()
							&& resource.isReadable()) {
						var path = ((ClassPathResource) resource).getPath();
						/* We have to manipulate the baseHref of the NG app to match the current servers contextPath */
						if (path.equals("static/index.html")) {
							String html = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
							String newBaseHref = String.format("base href=\"%s/\"", httpServletRequest.getContextPath());
							html = html.replace("base href=\"/\"", newBaseHref);
							return new TransformedResource(resource, html.getBytes());
						}
					}
					return resource;
				});
	}
}
