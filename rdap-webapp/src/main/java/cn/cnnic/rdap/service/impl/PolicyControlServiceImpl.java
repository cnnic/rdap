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
package cn.cnnic.rdap.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import cn.cnnic.rdap.bean.BaseSearchModel;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.BaseModel;
import cn.cnnic.rdap.bean.PolicyFields;
import cn.cnnic.rdap.dao.PolicyDao;
import cn.cnnic.rdap.service.PolicyControlService;

/**
 * policy control service implementation.
 * 
 * @author weijunkai
 * 
 */
@Service
public class PolicyControlServiceImpl implements PolicyControlService {

	/**
	 * PolicyDao.
	 */
	@Autowired
	private PolicyDao policyDao;

	@Override
	public void loadAllPolicyByMap() {
		PolicyFields policyFields = PolicyFields.getInstance();
		if (policyFields == null) {
			return;
		}
		policyFields.setMapObjField(policyDao.loadAllPolicyMap());
		return;
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
		strObjType = modelType.toString();
		strObjType = StringUtils.lowerCase(strObjType);
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
		if (!strMethod.startsWith(strSet) 
				&& !strMethod.startsWith(strGet)
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
		strMethodField = strMethodField.replaceFirst(
				strFieldFirstLetter, strReplace);

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
	private void setPropertyNull(final Object objModel,
			final String strField) {
		try {
			PropertyUtils.setProperty(objModel, strField, null);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * get the polic fields to hide.
	 * 
	 * @return map of fields.
	 */
	private Map<String, Set<String>> getPolicyFields() {
		PolicyFields hidCol = PolicyFields.getInstance();
		if (hidCol == null) {
			return null;
		}
		Map<String, Set<String>> mapObjFields = hidCol.getMapObjField();
		mapObjFields = policyDao.loadAllPolicyMap();

		return mapObjFields;
	}

	@Override
	public void applyPolicy(final Object objModel) {
		if (objModel == null) {
			return;
		}
		Map<String, Set<String>> mapObjFields = getPolicyFields();
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

		Method[] allMethods = ReflectionUtils.getUniqueDeclaredMethods(objModel
				.getClass());
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
			//just handle the specified method except boolean method
			if (!strIsBool.contains("Boolean") && isGetMethod) {
				value = getPropertyValue(objModel, strMethodField);
			}
			if (setFields != null) {
				Iterator<String> iter = setFields.iterator();
				while (iter.hasNext()) {
					String strField = iter.next();
					//just handle the set method
					if (strMethodField.compareToIgnoreCase(strField) == 0
							&& isSetMethod) {
						setPropertyNull(objModel, strField);
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
