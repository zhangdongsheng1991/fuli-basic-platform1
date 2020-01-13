package com.fuli.cloud.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支持级联属性
 * 
 * @author chenyi
 */
@Slf4j
public final class MyBeanUtil {

	/** yyyy-MM-dd'T'HH:mm:ss */
	private static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
	/** yyyy-MM-dd HH:mm:ss */
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static Class getSuperClassGenericType(Class clazz, int index) {
		// 得到泛型父类
		Type genType = clazz.getGenericSuperclass();
		// 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		// 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如BuyerServiceBean extends
		// DaoSupport就返回Buyer和Contact类型
		Type[] parameters = ((ParameterizedType) genType).getActualTypeArguments();
		if (index > parameters.length || index < 0) {
			throw new RuntimeException("你输入的索引号" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
		}
		if (!(parameters[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) parameters[index];
	}

	public static Class getSuperClassGenericType(Class clazz) {
		return getSuperClassGenericType(clazz, 0);
	}

	public static Class getMethodGenericReturnType(Method method, int index) {
		Type returnType = method.getGenericReturnType();
		if (returnType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) returnType;
			Type[] typeArguments = type.getActualTypeArguments();
			if (index >= typeArguments.length || index < 0) {
				throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
			}
			return (Class) typeArguments[index];
		}
		return Object.class;
	}

	public static Class getMethodGenericReturnType(Method method) {
		return getMethodGenericReturnType(method, 0);
	}

	public static List getMethodGenericParameterTypes(Method method, int index) {
		List results = new ArrayList();
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		if (index > genericParameterTypes.length || index < 0) {
			throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
		}
		Type genericParamenterType = genericParameterTypes[index];
		if (genericParamenterType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericParamenterType;
			Type[] parameterArgTypes = aType.getActualTypeArguments();
			for (Type parameterArgType : parameterArgTypes) {
				Class parameterArgClass = (Class) parameterArgType;
				results.add(parameterArgClass);
			}
			return results;
		}
		return results;
	}

	public static List getMethodGenericParameterTypes(Method method) {
		return getMethodGenericParameterTypes(method, 0);
	}

	public static Class getFieldGenericType(Field field, int index) {
		Type genericFileType = field.getGenericType();
		if (genericFileType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericFileType;
			Type[] fieldArgTypes = aType.getActualTypeArguments();
			if (index > fieldArgTypes.length || index < 0) {
				throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
			}
			return (Class) fieldArgTypes[index];
		}
		return Object.class;
	}

	public static Class getFieldGenericType(Field field) {
		return getFieldGenericType(field, 0);
	}

	public static <T> List<T> mapToEntity(Class<T> clazz, Collection<Map<String, Object>> propertiesCol,
			String... ormPackageNames) {

		if (null == propertiesCol) {
			return Collections.EMPTY_LIST;
		}
		List<T> entities = new ArrayList<T>();
		for (Map<String, Object> properties : propertiesCol) {
			entities.add(mapToEntity(clazz, properties, ormPackageNames));
		}
		return entities;
	}

	/**
	 * 
	 * @param clazz 需要映射的实体Class类型
	 * @param properties  实体的数据
	 * @param ormPackageNames
	 * @return
	 */
	public static <T> T mapToEntity(Class<T> clazz, Map<String, Object> properties, String... ormPackageNames) {

		if (MapUtils.isEmpty(properties)) {
			return null;
		}
		T entity = null;
		try {
			entity = clazz.newInstance();
			Class<?> classType = clazz;
			// 得到所有的fields
			Field[] fs = classType.getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				String fieldName = f.getName();
				Object fieldValue = properties.get(fieldName);
				if (null == fieldValue) {
					continue;
				}
				// 得到field的class
				Class fieldClazz = f.getType();
				// 【1】 //判断是否为基本类型 或者 lang包中的类型 或者 日期类型
				if (fieldClazz.isPrimitive() || fieldClazz.getName().startsWith("java.lang")) {
					BeanUtils.setProperty(entity, fieldName, fieldValue);
					continue;
				}
				if (Date.class.isAssignableFrom(fieldClazz)) {
					// 将String类型的转换为Date
					if (fieldValue instanceof String) {
						String strValue = ((String) fieldValue).trim();
						// 判断是不是一个空字符串
						if (strValue.equals("")) {
							continue;
						}
						// "updateTime" : "2019-04-22T03:36:10.000+0000",
						// 默认尝试连两种格式
						SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS);
						SimpleDateFormat formatter2 = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
						Date date = null;
						try {
							date = formatter.parse(strValue);
						} catch (Exception e) {
							try {
								date = formatter2.parse(strValue);
							} catch (Exception e1) {
								throw new RuntimeException(e1);
							}
						}
						BeanUtils.setProperty(entity, fieldName, date);
						continue;
					}
				}
				// 【2】若为自定义的orm // com.tomtop.application.orm
				if (isSelfDesignOrm(fieldClazz, ormPackageNames)) {
					Object obj = mapToEntity(fieldClazz, (Map<String, Object>) fieldValue, ormPackageNames);
					if (null != obj) {
						BeanUtils.setProperty(entity, fieldName, obj);
					}
					continue;
				}
				// 【3】 如果是Collection类型，得到其Generic的类型
				if (Collection.class.isAssignableFrom(fieldClazz)) {
					Collection col = handleCollection(f.getGenericType(), (Collection) fieldValue, ormPackageNames);
					if (null != col) {
						BeanUtils.setProperty(entity, fieldName, col);
					}
					continue;
				}
				// 【4】 如果是Map类型，得到其Generic的类型
				if (Map.class.isAssignableFrom(fieldClazz)) {
					Map map = handleMap(f.getGenericType(), (Map) fieldValue, ormPackageNames);
					if (null != map) {
						BeanUtils.setProperty(entity, fieldName, map);
					}
					continue;
				}
				// 其他,好自为之~
				BeanUtils.setProperty(entity, fieldName, fieldValue);
			}

		} catch (Exception e) {
			log.error("mapToEntity",e);
		}
		return entity;
	}

	private static <T> Collection handleCollection(Type fGenericType, Collection colProperties,String... ormPackageNames) throws Exception {
		if (colProperties == null) {
			return null;
		}
		if (fGenericType == null) {
			return null;
		}
		Collection col = new ArrayList();
		if (fGenericType instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) fGenericType;
			Type acType = pt.getActualTypeArguments()[0];
			Type rowTpye = pt.getRawType();
			if (rowTpye instanceof Class) {
				Class rtClass = (Class) rowTpye;
				col = newCollectionByType(rtClass);
				if (acType instanceof ParameterizedType) {
					ParameterizedType acParameterizedType = (ParameterizedType) acType;
					Type acRowTpye = acParameterizedType.getRawType();
					if (acRowTpye instanceof Class) {
						Class acRowTpyeClass = (Class) acRowTpye;
						// 若List的元素为自定义的orm，应该做递归处理
						if (isSelfDesignOrm(acRowTpyeClass, ormPackageNames)) {
							Collection<Map<String, Object>> listMap = colProperties;
							for (Map entityMap : listMap) {
								col.add(mapToEntity(acRowTpyeClass, entityMap, ormPackageNames));
							}
						} else if (Collection.class.isAssignableFrom(acRowTpyeClass)) {
							Collection colResult = null;
							for (Collection coll : (Collection<Collection>) colProperties) {
								colResult = handleCollection(acParameterizedType, coll, ormPackageNames);
								if (null != colResult) {
									col.add(colResult);
								}
							}
						} else if (Map.class.isAssignableFrom(acRowTpyeClass)) {
							Map mapResult = null;
							for (Map map : (Collection<Map>) colProperties) {
								mapResult = handleMap(acParameterizedType, map, ormPackageNames);
								if (null != mapResult) {
									col.add(mapResult);
								}
							}
						} // 其他，不造怎么办老~~
					}
					// 到达最内层的泛型参数
				} else if (acType instanceof Class) {
					Class acTpyeClass = (Class) acType;
					if (!isSelfDesignOrm(acTpyeClass, ormPackageNames)) {
						// 基本类型直接可以用BeanUtils.setProperty,
						// 但json字符串bean转换为Map的过程中,javaBean中List 和 Set 都会用ArrayList封装
						// 所以要自己转换一下，否则报错type mismatch，妈蛋~~
						col.addAll(colProperties);
					} else {
						// 若List的元素为自定义的orm，应该做递归处理
						Collection<Map<String, Object>> listMap = colProperties;
						for (Map entityMap : listMap) {
							col.add(mapToEntity(acTpyeClass, entityMap, ormPackageNames));
						}
					}
				}
			}
		} else {
			System.out.println("fGenericType is removed ！！！");
			return colProperties;
		}
		return col;
	}

	private static Collection newCollectionByType(Class rtClass) {
		Collection col = new ArrayList();
		if (List.class.isAssignableFrom(rtClass)) {
			if (LinkedList.class.isAssignableFrom(rtClass)) {
				col = new LinkedList();
			} else {
				col = new ArrayList();
			}
		} else if (Set.class.isAssignableFrom(rtClass)) {
			if (HashSet.class.isAssignableFrom(rtClass)) {
				col = new HashSet();
			} else if (LinkedHashSet.class.isAssignableFrom(rtClass)) {
				col = new LinkedHashSet();
			} else if (TreeSet.class.isAssignableFrom(rtClass)) {
				col = new TreeSet();
			} else {
				col = new HashSet();
			}
		}
		return col;
	}

	private static <T> Map handleMap(Type fGenericType, Map mapProperties, String... ormPackageNames) throws Exception {

		if (mapProperties == null) {
			return null;
		}
		if (fGenericType == null) {
			return null;
		}
		Map map = null;
		if (fGenericType instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) fGenericType;
			Class rowTpyeClass = (Class) pt.getRawType();
			Type acTypeVal = pt.getActualTypeArguments()[1];
			map = newMapByType(rowTpyeClass);
			// map的key应该不会级联泛型吧！~ ！暂时只考虑
			if (acTypeVal instanceof ParameterizedType) {
				ParameterizedType acTypeValPT = (ParameterizedType) acTypeVal;
				Type acTypeValRowType = acTypeValPT.getRawType();
				if (acTypeValRowType instanceof Class) {
					Class acRowTypeClass = (Class) acTypeValRowType;
					// 若为自定义的orm，应该做递归处理
					if (isSelfDesignOrm(acRowTypeClass, ormPackageNames)) {
						Map value = null;
						Object valOrm = null;
						for (Object key : mapProperties.keySet()) {
							if (null == key) {
								return null;
							}
							value = (Map) mapProperties.get(key);
							if (null == value) {
								return null;
							}
							valOrm = mapToEntity(acRowTypeClass, value, ormPackageNames);
							if (null != valOrm) {
								map.put(key, valOrm);
							}
						}
					} else if (Collection.class.isAssignableFrom(acRowTypeClass)) {
						Collection value = null;
						Collection valCol = null;
						for (Object key : mapProperties.keySet()) {
							if (null == key) {
								return null;
							}
							value = (Collection) mapProperties.get(key);
							if (null == value) {
								return null;
							}
							valCol = handleCollection(acTypeValPT, value, ormPackageNames);
							if (null != valCol) {
								map.put(key, valCol);
							}
						}
					} else if (Map.class.isAssignableFrom(acRowTypeClass)) {
						Map value = null;
						Map valMap = null;
						for (Object key : mapProperties.keySet()) {
							if (null == key) {
								return null;
							}
							value = (Map) mapProperties.get(key);
							if (null == value) {
								return null;
							}
							valMap = handleMap(acTypeValPT, value, ormPackageNames);
							if (null != valMap) {
								map.put(key, valMap);
							}
						}
					} // 其他，不造怎么办老~~
				}
			} else { // Class 到达最内层的泛型参数
				Class valClass = (Class) acTypeVal;
				// 若Map的value为自定义的orm，应该做递归处理
				if (isSelfDesignOrm(valClass, ormPackageNames)) {
					Map value = null;
					Object valOrm = null;
					for (Object key : mapProperties.keySet()) {
						if (null == key) {
							return null;
						}
						value = (Map) mapProperties.get(key);
						if (null == value) {
							return null;
						}
						valOrm = mapToEntity(valClass, value, ormPackageNames);
						if (null != valOrm) {
							map.put(key, valOrm);
						}
					}
				} else if (Collection.class.isAssignableFrom(valClass)) {
					System.out.println(valClass);
				} else if (Map.class.isAssignableFrom(valClass)) {
					System.out.println(valClass);
				} else {
					for (Object key : mapProperties.keySet()) {
						map.put(key, mapProperties.get(key));
					}
				}
			}
		} else {
			System.out.println("fGenericType is removed ！！！");
			return mapProperties;
		}
		return map;
	}

	private static Map newMapByType(Class rowTpyeClass) {
		Map map;
		if (HashMap.class.isAssignableFrom(rowTpyeClass)) {
			map = new HashMap();
		} else if (LinkedHashMap.class.isAssignableFrom(rowTpyeClass)) {
			map = new LinkedHashMap();
		} else if (TreeMap.class.isAssignableFrom(rowTpyeClass)) {
			map = new TreeMap();
		} else {
			map = new HashMap();
		}
		return map;
	}

	/**
	 * 先判断包名是否以系统自定义的orm包的前缀，若是直接返回true， 否则在判断是否包含在自己提供的orm包名内
	 * 
	 * @param clazz
	 * @param ormPackageNames 自定义orm的包名
	 * @return true:是自定义orm false:不是自定义orm
	 */
	public static boolean isSelfDesignOrm(Class<?> clazz, String... ormPackageNames) {
		List<String> packages = Arrays.asList("com.fuli.cloud.model", "com.fuli.cloud.message.provider.model");
		String clazzPackage = clazz.getPackage().getName();
		for (String packageName : packages) {
			if (clazzPackage.startsWith(packageName)) {
				return true;
			}
		}
		if (null == ormPackageNames || ormPackageNames.length == 0) {
			return false;
		}
		for (String packageName : ormPackageNames) {
			if (clazzPackage.startsWith(packageName)) {
				return true;
			}
		}
		return false;
	}

}
