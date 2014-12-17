package net.gnomeffinway.depenizen.support;

import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Supported {

    private final static Class<Supported> clazz = Supported.class;
    private static Depenizen depenizen;
    private static SupportManager supportManager;
    private static Map<String, Class<? extends Support>> supportClasses;

    public static void setup(Depenizen dep, PluginManager pluginManager, ClassLoader loader) {
        depenizen = dep;
        getSupportClasses(loader);
        supportManager = new SupportManager(depenizen);
        for (String name : depenizen.getDescription().getSoftDepend()) {
            if (set(name.toUpperCase(), pluginManager.getPlugin(name)))
                Depenizen.depenizenLog(name + " hooked, enabling add-ons.");
            else
                Depenizen.depenizenLog(name + " not found, add-ons will not enable.");
        }
        supportManager.registerNewObjects();
    }

    private static boolean set(String name, Plugin plugin) {
        try {
            if (plugin == null || !plugin.isEnabled()) return false;
            supportManager.register(supportClasses.get(name).newInstance().setPlugin(plugin));
            return true;
        } catch (IllegalAccessException e) {
            dB.echoError("Report this error to Morphan1 or the Denizen dev team: SupportedIllegalAccess");
            dB.echoError(e);
        } catch (InstantiationException e) {
            dB.echoError("Report this error to Morphan1 or the Denizen dev team: SupportedInstantiationSupport" + name);
            dB.echoError(e);
        }
        return false;
    }

    private static void getSupportClasses(ClassLoader loader) {
        supportClasses = new HashMap<String, Class<? extends Support>>();
        final String pkg = "net.gnomeffinway.depenizen.support.plugins";
        try {
            final Enumeration<URL> resources = loader.getResources(pkg.replace('.', '/'));
            URL url;
            while (resources.hasMoreElements()) {
                url = resources.nextElement();
                if (url == null) continue;
                URLConnection conn = url.openConnection();
                if (conn instanceof JarURLConnection) {
                    final JarFile jarFile = ((JarURLConnection) conn).getJarFile();
                    final Enumeration<JarEntry> entries = jarFile.entries();
                    String name;
                    while (entries.hasMoreElements()) {
                        name = entries.nextElement().getName().replace('/', '.');
                        if (name.startsWith(pkg) && name.endsWith("Support.class")) {
                            String key = name.replace("Support.class", "");
                            key = key.substring(key.lastIndexOf('.')+1).toUpperCase();
                            supportClasses.put(key,
                                    (Class<? extends Support>) Class.forName(name.substring(0, name.lastIndexOf('.'))));
                        }
                    }
                }
            }
        } catch (Exception e) {
            dB.echoError(e);
        }
    }
}
