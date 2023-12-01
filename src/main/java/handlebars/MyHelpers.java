package handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class MyHelpers {

    public static void registerHelpers(Handlebars handlebars) {
        handlebars.registerHelper("eq", new Helper<Object>() {
            @Override
            public Object apply(Object context, Options options) {
                if (context == null || options.params == null || options.params.length == 0) {
                    return false;
                }

                Object other = options.params[0];
                return context.equals(other);
            }
        });

        handlebars.registerHelper("or", new Helper<Object>() {
            @Override
            public Object apply(Object context, Options options) {
                for (Object param : options.params) {
                    if (isTruthy(param)) {
                        return true;
                    }
                }
                return false;
            }

            private boolean isTruthy(Object value) {
                if (value == null) {
                    return false;
                } else if (value instanceof Boolean) {
                    return (Boolean) value;
                } else if (value instanceof String) {
                    return !((String) value).isEmpty();
                }
                return true; // Otros casos, por ejemplo, números, se consideran verdaderos
            }
        });
    }
}

