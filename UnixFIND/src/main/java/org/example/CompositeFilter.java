package org.example;

import java.util.List;

abstract class CompositeFilter implements Filter {
    protected final List<Filter> filters;

    protected CompositeFilter(List<Filter> filters) {
        this.filters = filters;
    }
}
class AndFilter extends CompositeFilter {

    public AndFilter(List<Filter> filters) {
        super(filters);
    }

    @Override
    public boolean matches(FileSystemNode file) {
        for (Filter filter : filters) {
            if (!filter.matches(file)) {
                return false;
            }
        }
        return true;
    }
}
class OrFilter extends CompositeFilter {

    public OrFilter(List<Filter> filters) {
        super(filters);
    }

    @Override
    public boolean matches(FileSystemNode file) {
        for (Filter filter : filters) {
            if (filter.matches(file)) {
                return true;
            }
        }
        return false;
    }
}
class NotFilter implements Filter {

    private final Filter filter;

    public NotFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public boolean matches(FileSystemNode file) {
        return !filter.matches(file);
    }
}

