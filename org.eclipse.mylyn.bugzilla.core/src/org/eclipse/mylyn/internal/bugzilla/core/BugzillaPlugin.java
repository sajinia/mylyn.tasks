/*******************************************************************************
 * Copyright (c) 2003 - 2006 University Of British Columbia and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     University Of British Columbia - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylar.internal.bugzilla.core;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylar.provisional.tasklist.TaskRepository;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Mik Kersten (added support for multiple repositories)
 */
public class BugzillaPlugin extends Plugin {

	public static final String REPOSITORY_KIND = "bugzilla";

	public static final String ENCODING_UTF_8 = "UTF-8";

	public static final String PLUGIN_ID = "org.eclipse.mylar.bugzilla";

	/** Singleton instance of the plug-in */
	private static BugzillaPlugin plugin;

	// /** The file that contains all of the bugzilla favorites */
	// private FavoritesFile favoritesFile;

	/** Product configuration for the current server */
	private Map<String, RepositoryConfiguration> repositoryConfigurations = new HashMap<String, RepositoryConfiguration>();

	public BugzillaPlugin() {
		super();
	}

	/**
	 * Get the singleton instance for the plugin
	 * 
	 * @return The instance of the plugin
	 */
	public static BugzillaPlugin getDefault() {
		return plugin;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// readFavoritesFile();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	// /**
	// * Get the favorites file contatining the favorites
	// *
	// * @return The FavoritesFile
	// */
	// public FavoritesFile getFavorites() {
	// return favoritesFile;
	// }

	// /**
	// * // * Get the name of the bugzilla server // * // *
	// *
	// * @return A string containing the prefered name of the bugzilla server //
	// */
	// // public String getServerName() {
	// // return
	// //
	// plugin.getPreferenceStore().getString(IBugzillaConstants.BUGZILLA_SERVER);
	// // }
	// public boolean isServerCompatability218() {
	// return
	// IBugzillaConstants.SERVER_218.equals(getPreferenceStore().getString(IBugzillaConstants.SERVER_VERSION))
	// || IBugzillaConstants.SERVER_220.equals(getPreferenceStore().getString(
	// IBugzillaConstants.SERVER_VERSION));
	// }
	//
	// public boolean isServerCompatability220() {
	// return
	// IBugzillaConstants.SERVER_220.equals(getPreferenceStore().getString(IBugzillaConstants.SERVER_VERSION));
	// }

	public RepositoryConfiguration getProductConfiguration(TaskRepository repository) throws IOException {
		if (!repositoryConfigurations.containsKey(repository.getUrl())) {
			repositoryConfigurations.put(repository.getUrl(), RepositoryConfigurationFactory.getInstance()
					.getConfiguration(repository));
		}
		return repositoryConfigurations.get(repository.getUrl());
	}

	// public RepositoryConfiguration getProductConfiguration(String serverUrl)
	// {
	// if (!repositoryConfigurations.containsKey(serverUrl)) {
	// try {
	// repositoryConfigurations.put(serverUrl,
	// RepositoryConfigurationFactory.getInstance().getConfiguration(
	// serverUrl));
	// } catch (IOException e) {
	// MessageDialog.openInformation(null, "Retrieval of Bugzilla
	// Configuration",
	// "Bugzilla configuration retrieval failed.");
	// }
	// }
	//
	// return repositoryConfigurations.get(serverUrl);
	// }

	protected void setProductConfiguration(String serverUrl, RepositoryConfiguration repositoryConfiguration) {
		repositoryConfigurations.put(serverUrl, repositoryConfiguration);
		// this.productConfiguration = productConfiguration;
	}

	// private void readFavoritesFile() {
	// IPath favoritesPath = getFavoritesFile();
	//
	// try {
	// favoritesFile = new FavoritesFile(favoritesPath.toFile());
	// } catch (Exception e) {
	// logAndShowExceptionDetailsDialog(e, "occurred while restoring saved
	// Bugzilla favorites.",
	// "Bugzilla Favorites Error");
	// }
	// }

	// /**
	// * Returns the path to the file cacheing the query favorites.
	// */
	// private IPath getFavoritesFile() {
	// IPath stateLocation =
	// Platform.getStateLocation(BugzillaPlugin.getDefault().getBundle());
	// IPath configFile = stateLocation.append("favorites");
	// return configFile;
	// }

	// /**
	// * Reads cached product configuration and stores it in the
	// * <code>productConfiguration</code> field.
	// *
	// * TODO remove this?
	// */
	// private void readCachedProductConfiguration(String serverUrl) {
	// IPath configFile = getProductConfigurationCachePath(serverUrl);
	//
	// try {
	// productConfigurations.put(serverUrl,
	// ServerConfigurationFactory.getInstance().readConfiguration(
	// configFile.toFile()));
	// } catch (IOException ex) {
	// try {
	// log(ex);
	// productConfigurations.put(serverUrl,
	// ServerConfigurationFactory.getInstance().getConfiguration(
	// serverUrl));
	// } catch (IOException e) {
	// log(e);
	// MessageDialog
	// .openInformation(
	// null,
	// "Bugzilla product attributes check",
	// "An error occurred while restoring saved Bugzilla product attributes:
	// \n\n"
	// + ex.getMessage()
	// + "\n\nUpdating them from the server also caused an error:\n\n"
	// + e.getMessage()
	// + "\n\nCheck the server URL in Bugzila preferences.\n"
	// + "Offline submission of new bugs will be disabled until valid product
	// attributes have been loaded.");
	// }
	// }
	// }

	/**
	 * Returns the path to the file cacheing the product configuration.
	 */
	protected IPath getProductConfigurationCachePath(String serverUrl) {
		IPath stateLocation = Platform.getStateLocation(BugzillaPlugin.getDefault().getBundle());
		IPath configFile = stateLocation.append("productConfig." + serverUrl.replace('/', '-'));
		return configFile;
	}

	/**
	 * Convenience method for logging statuses to the plugin log
	 * 
	 * @param status
	 *            the status to log
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	/**
	 * Convenience method for logging exceptions to the plugin log
	 * 
	 * @param e
	 *            the exception to log
	 */
	public static void log(Exception e) {
		log(new Status(Status.ERROR, BugzillaPlugin.PLUGIN_ID, 0, e.getMessage(), e));
	}

	/**
	 * Returns the path to the file caching bug reports created while offline.
	 */
	protected IPath getCachedBugReportPath() {
		IPath stateLocation = Platform.getStateLocation(BugzillaPlugin.getDefault().getBundle());
		IPath bugFile = stateLocation.append("bugReports");
		return bugFile;
	}

	/**
	 * @param url
	 * @param proxy
	 *            can be null
	 */
	public URLConnection getUrlConnection(URL url, Proxy proxy) throws IOException, NoSuchAlgorithmException,
			KeyManagementException {
		SSLContext ctx = SSLContext.getInstance("TLS");

		javax.net.ssl.TrustManager[] tm = new javax.net.ssl.TrustManager[] { new TrustAll() };
		ctx.init(null, tm, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

		if (proxy == null) {
			proxy = Proxy.NO_PROXY;
		}
		URLConnection connection = url.openConnection(proxy);
		return connection;
	}

	// public IStatus logAndShowExceptionDetailsDialog(Exception e, String
	// message, String title) {
	// MultiStatus status = new MultiStatus(BugzillaPlugin.PLUGIN_ID,
	// IStatus.ERROR, e.getClass().toString() + " "
	// + message + "\n\n" + "Click Details or see log for more information.",
	// e);
	// Status s = new Status(IStatus.ERROR, BugzillaPlugin.PLUGIN_ID,
	// IStatus.ERROR, e.getClass().toString()
	// + ": ", e);
	// status.add(s);
	// String error = (e.getMessage() == null) ? e.getClass().toString() :
	// e.getMessage();
	// s = new Status(IStatus.ERROR, BugzillaPlugin.PLUGIN_ID, IStatus.ERROR,
	// error, e);
	// status.add(s);
	// log(status);
	// ErrorDialog.openError(null, title, null, status);
	// return status;
	// }

	// public boolean refreshOnStartUpEnabled() {
	// return getPreferenceStore().getBoolean(IBugzillaConstants.REFRESH_QUERY);
	// }

	// private void setDefaultQueryOptions() {
	// // get the preferences store for the bugzilla preferences
	// IPreferenceStore prefs = getPreferenceStore();
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_STATUS, BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_STATUS_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUSE_STATUS_PRESELECTED,
	// BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_PRESELECTED_STATUS_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_RESOLUTION,
	// BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_RESOLUTION_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_SEVERITY,
	// BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_SEVERITY_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_PRIORITY,
	// BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_PRIORITY_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_HARDWARE,
	// BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_HARDWARE_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_OS, BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_OS_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_PRODUCT,
	// BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_PRODUCT_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_COMPONENT,
	// BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_COMPONENT_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_VERSION,
	// BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_VERSION_VALUES));
	//
	// prefs.setDefault(IBugzillaConstants.VALUES_TARGET, BugzillaRepositoryUtil
	// .queryOptionsToString(IBugzillaConstants.DEFAULT_TARGET_VALUES));
	// }

}
