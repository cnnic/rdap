/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.restfulwhois.rdap.filters.queryFilter.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.model.base.BaseModel;
import org.restfulwhois.rdap.core.common.model.base.BaseSearchModel;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.restfulwhois.rdap.filters.queryFilter.dao.CustomColumnPolicyDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

/**
 * registry policy control service implementation.
 * 
 * Requirement from
 * http://tools.ietf.org/html/draft-ietf-weirds-rdap-sec-06#section-3.4
 * 
 * registry rules for disclosure of certain object .
 * 
 * @author weijunkai
 * 
 */
@Service
public class CustomColumnPolicyService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CustomColumnPolicyService.class);

    /**
     * CustomColumnPolicyDao.
     */
    @Autowired
    private CustomColumnPolicyDao policyDao;

    /**
     * the static map of policy.
     */
    private static Map<String, Set<String>> mapPolicy = null;

    public Map<String, Set<String>> loadPolicyFieldsByMap() {
        return mapPolicy;
    }

    @PostConstruct
    public void init() {
        mapPolicy = policyDao.loadAllPolicyMap();
        return;
    }

    /**
     * clear policy.
     */
    public void clearPolicy() {
        mapPolicy.clear();
        mapPolicy = null;
    }

    /**
     * get Model String.
     * 
     * @param objModel
     *            the model of object.
     * @return string of object type.
     */
    private String getModelString(final Object objModel) {
        String strObjType = null;

        ModelType modelType = ((BaseModel) objModel).getObjectType();
        strObjType = modelType.getName();
        if (strObjType == "errorMessage") {
            LOGGER.debug("getModelString, objModel is errorMessage");
            return null;
        }
        return strObjType;
    }

    /**
     * trucate the string.
     * 
     * @param strMethod
     *            set the method string trucated.
     * @return string of method.
     */
    private String trucateStringFromMethod(String strMethod) {

        final String strSet = "set";
        final String strGet = "get";
        final String strIs = "is";
        if (!strMethod.startsWith(strSet) && !strMethod.startsWith(strGet)
                && !strMethod.startsWith(strIs)) {
            return null;
        }

        int posAfterSet = strSet.length();
        if (strMethod.startsWith(strIs)) {
            posAfterSet = strIs.length();
        }

        String strMethodField = strMethod.substring(posAfterSet);
        String strFieldFirstLetter = strMethodField.substring(0, 1);
        String strReplace = StringUtils.lowerCase(strFieldFirstLetter);
        strMethodField =
                strMethodField.replaceFirst(strFieldFirstLetter, strReplace);

        return strMethodField;
    }

    /**
     * set the property.
     * 
     * @param objModel
     *            the object to set.
     * @param strField
     *            the field to set.
     */
    private void setPropertyNull(final Object objModel, final String strField) {
        try {
            PropertyUtils.setProperty(objModel, strField, null);
        } catch (IllegalAccessException e) {
            LOGGER.error("setPropertyNull ", e.getMessage());
        } catch (InvocationTargetException e) {
            LOGGER.error("setPropertyNull ", e.getMessage());
        } catch (NoSuchMethodException e) {
            LOGGER.error("setPropertyNull ", e.getMessage());
        }
    }

    /**
     * get the baseModel property value.
     * 
     * @param objModel
     *            model of object.
     * @param strMethodField
     *            string of method field.
     * @return the object of inner object.
     */
    private Object getPropertyValue(final Object objModel,
            final String strMethodField) {
        Object value = null;
        try {
            value = PropertyUtils.getProperty(objModel, strMethodField);
        } catch (IllegalAccessException e) {
            LOGGER.error("getPropertyValue ", e.getMessage());
        } catch (InvocationTargetException e) {
            LOGGER.error("getPropertyValue ", e.getMessage());
        } catch (NoSuchMethodException e) {
            LOGGER.error("getPropertyValue ", e.getMessage());
        }
        return value;
    }

    /**
     * get the baseModel property value.
     * 
     * @param objModel
     *            model of object.
     */
    public void applyPolicy(final Object objModel) {
        if (objModel == null) {
            return;
        }
        Map<String, Set<String>> mapObjFields = loadPolicyFieldsByMap();
        if (null == mapObjFields) {
            return;
        }
        String strObjType = null;
        if (objModel.getClass().getSuperclass() == BaseModel.class
                || objModel.getClass().getSuperclass() == BaseSearchModel.class) {
            strObjType = getModelString(objModel);
        }
        if (strObjType == null) {
            return;
        }
        Set<String> setFields = mapObjFields.get(strObjType);

        Method[] allMethods =
                ReflectionUtils.getUniqueDeclaredMethods(objModel.getClass());
        for (Method mthd : allMethods) {
            String strMethod = mthd.getName();
            String strMethodField = trucateStringFromMethod(strMethod);
            if (strMethodField == null) {
                continue;
            }
            Object value = null;
            final String strSetFirstWord = "s";
            boolean isSetMethod = strMethod.startsWith(strSetFirstWord);
            boolean isGetMethod = strMethod.startsWith("g");
            String strIsBool = mthd.getReturnType().toString();
            // just handle the specified method except boolean ones
            if (!strIsBool.contains("Boolean") && isGetMethod) {
                value = getPropertyValue(objModel, strMethodField);
            }
            if (setFields != null) {
                Iterator<String> iter = setFields.iterator();
                while (iter.hasNext()) {
                    String strField = iter.next();
                    // just handle the set method
                    if (strMethodField.equalsIgnoreCase(strField)
                            && isSetMethod) {
                        setPropertyNull(objModel, strMethodField);
                        break;
                    }
                }
            }
            if (value == null) {
                continue;
            }
            if (mthd.getReturnType() == List.class) {
                setInnerListPolicy(value);
            } else {
                applyPolicy(value);
            }
        }
        return;
    }

    /**
     * set the inner object policy.
     * 
     * @param object
     *            object to set.
     */
    private void setInnerListPolicy(final Object object) {

        List<?> listObjs = (List<?>) object;
        if (listObjs != null) {
            for (int iObj = 0; iObj < listObjs.size(); ++iObj) {
                applyPolicy(listObjs.get(iObj));
            }
        }
    }
}
