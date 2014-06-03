package cn.cnnic.rdap.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * page bean for paging.
 * 
 * @author jiashuo
 * 
 */
public class PageBean implements java.io.Serializable {
    /**
     * serial id.
     */
    private static final long serialVersionUID = 693435998917420886L;
    /**
     * first page num.
     */
    public static final int FIRST_PAGE_NUM = 1;
    /**
     * parameter map.
     */
    private Map<String, Object> parameterMap;
    /**
     * current page.
     */
    private int currentPage = FIRST_PAGE_NUM;
    /**
     * total records count.
     */
    private int recordsCount = -1;
    /**
     * max records per page.
     */
    private Integer maxRecords = 5;
    /**
     * page count.
     */
    private int pageCount;

    /**
     * Creates a new PageBean object.
     */
    public PageBean() {
    }

    /**
     * check if last page.
     * 
     * @return true if is, false if not.
     */
    public boolean isNotLastPage() {
        return getCurrentPage() <= (getRecordsCount() / getMaxRecords()) + 1;
    }

    /**
     * Creates a new PageBean object.
     * 
     * @param currentPage
     *            :current page
     * @param maxRecords
     *            :max records of page
     */
    public PageBean(int currentPage, int maxRecords) {
        this.currentPage = currentPage;
        this.maxRecords = maxRecords;
    }

    /**
     * Creates a new PageBean object.
     * 
     * @param maxRecords
     *            :max records of page
     */
    public PageBean(int maxRecords) {
        this.maxRecords = maxRecords;
    }

    /**
     * Creates a new PageBean object.
     * 
     * @param currentPage
     *            current page.
     * @param recordsCount
     *            total count.
     * @param params
     *            params.
     */
    public PageBean(int currentPage, int recordsCount,
            Map<String, Object> params) {
        this.currentPage = currentPage;
        this.recordsCount = recordsCount;
        this.parameterMap = params;
    }

    /**
     * put params to map.
     * 
     * @param key
     *            param key.
     * @param value
     *            param value.
     */
    public void putParameter(String key, Object value) {
        if (parameterMap == null) {
            parameterMap = new HashMap<String, Object>();
        }
        parameterMap.put(key, value);
    }

    /**
     * get current page.
     * 
     * @return current page.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * get total records count.
     * 
     * @return total records count.
     */
    public int getRecordsCount() {
        return recordsCount;
    }

    /**
     * get max records of page.
     * 
     * @return max records of page.
     */
    public Integer getMaxRecords() {
        return maxRecords;
    }

    /**
     * set current page.
     * 
     * @param currentPage
     *            :current page.
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * set records count.
     * 
     * @param recordsCount
     *            :records count.
     */
    public void setRecordsCount(int recordsCount) {
        this.recordsCount = recordsCount;
    }

    /**
     * set params map.
     * 
     * @param parameterMap
     *            :params map.
     */
    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    /**
     * set max records of page.
     * 
     * @param maxRecords
     *            :max records of page.
     */
    public void setMaxRecords(Integer maxRecords) {
        this.maxRecords = maxRecords;
    }

    /**
     * increment current page.
     */
    public void incrementCurrentPage() {
        this.currentPage++;
    }

    /**
     * to string.
     * 
     * @return string of bean.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("currentPage", currentPage)
                .append("maxRecords", maxRecords)
                .append("recordsCount", recordsCount)
                .append("parameterMap", parameterMap).toString();
    }

    /**
     * set page count.
     * 
     * @param pageCount
     *            : page count.
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * get page count.
     * 
     * @return: page count.
     */
    public int getPageCount() {
        return pageCount;
    }
}
