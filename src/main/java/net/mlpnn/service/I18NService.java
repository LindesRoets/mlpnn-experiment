package net.mlpnn.service;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lindes Roets
 */
@Service
public class I18NService implements MessageSourceAware {

	private MessageSource msg;

	@Override
	public void setMessageSource(MessageSource ms) {
		msg = ms;
	}

	public String getMessage(String key) {
		return msg.getMessage(key, new Object[]{}, LocaleContextHolder.getLocale());
	}
}
