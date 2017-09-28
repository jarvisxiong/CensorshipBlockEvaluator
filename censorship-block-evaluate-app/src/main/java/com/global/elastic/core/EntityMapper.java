/**
 * 
 */
package com.global.elastic.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ankit.gupta4
 *
 */
public class EntityMapper {

	/**
	 * The logger.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	/**
	 * Get the unique instnce of entity builder.
	 */
	private static EntityMapper instance;

	/**
	 * Json mapper.
	 */
	private ObjectMapper mapper;

	/**
	 * Default constructor.
	 */
	private EntityMapper() {
		mapper = new ObjectMapper();
	}

	/**
	 * Gets the entity builder.
	 * 
	 * @return the entity builder.
	 */
	public static EntityMapper getInstance() {
		if (instance == null) {
			instance = new EntityMapper();
		}
		return instance;
	}

	/**
	 * Return an object from json value.
	 *
	 * @param <T>
	 *            generic entity.
	 * @param id
	 *            the id
	 * @param json
	 *            the json value
	 * @param aClass
	 *            type of object.
	 * @return an object.
	 * @throws ElasticException
	 *             if an error occurs.
	 */
	public <T extends EsBean> T getObject(String id, String json, Class<T> aClass) throws ElasticException {
		try {
			T obj = mapper.readValue(json, aClass);
			obj.setId(id);
			return obj;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ElasticException(e);
		}
	}

	/**
	 * Serialize the EsBean.
	 *
	 * @param <T>
	 *            the generic object.
	 * @param bean
	 *            the bean
	 * @return the json value.
	 * @throws ElasticException
	 *             the elastic exception
	 */
	public <T extends EsBean> String getSerialize(T bean) throws ElasticException {
		try {
			return mapper.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			throw new ElasticException(e.getMessage(), e);
		}
	}

	/**
	 * Gets the fields.
	 *
	 * @param <T>
	 *            the generic type
	 * @param beanClass
	 *            the class name
	 * @return the fields
	 */
	public static <T extends EsBean> List<String> getFields(Class<T> beanClass) {
		List<String> privateFields = new ArrayList<>();
		Field[] allFields = beanClass.getDeclaredFields();
		for (Field field : allFields) {
			if (Modifier.isPrivate(field.getModifiers())
					&& field.getType().getSimpleName().equals(String.class.getSimpleName())) {
				if (field.getName().contains("date") || field.getName().contains("Date")) {
					continue;
				}
				privateFields.add(field.getName());
			}
		}
		return privateFields;
	}

}
