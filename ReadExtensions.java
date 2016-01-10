private void readExtensionPoint() {
		//Platform.getExtensionRegistry().getConfigurationElementsFor(CONTROL_EXTENSION) is used to 
		//get the list of controls specified in the extension
		final IConfigurationElement[] controls = Platform.getExtensionRegistry().getConfigurationElementsFor(
			CONTROL_EXTENSION);
		for (final IConfigurationElement e : controls) {
			try {
				final String id = e.getAttribute(CONTROL_ID);
				final String clazz = e.getAttribute(CLASS_ATTRIBUTE);
				//getContributor is used to get the bundle that defines the extension
				final Class<? extends ECPAbstractControl> resolvedClass = loadClass(e.getContributor().getName(), clazz);
				final boolean showLabel = Boolean.parseBoolean(e.getAttribute(LABEL_ATTRIBUTE));

				final Set<ECPApplicableTester> tester = new HashSet<ECPApplicableTester>();
				for (final IConfigurationElement testerExtension : e.getChildren()) {
					if (TEST_DYNAMIC.equals(testerExtension.getName())) {
					//createExecutableExtension is used to create an instance of the class specified in the extension
						tester.add((ECPApplicableTester) testerExtension.createExecutableExtension(CONTROL_TESTER));
					}
					else if (TEST_STATIC.equals(testerExtension.getName())) {
						final boolean singleValue = Boolean.parseBoolean(testerExtension
							.getAttribute(TESTER_SINGLEVALUE));
						final int priority = Integer.parseInt(testerExtension.getAttribute(TESTER_PRIORITY));

						final String type = testerExtension.getAttribute(TESTER_CLASSTYPE);
						final Class<?> supportedClassType = loadClass(testerExtension.getContributor().getName(), type);

						String eObject = testerExtension.getAttribute(TESTER_EOBJECT);
						if (eObject == null) {
							eObject = "org.eclipse.emf.ecore.EObject";//$NON-NLS-1$
						}
						final Class<? extends EObject> supportedEObject = loadClass(testerExtension.getContributor()
							.getName(), eObject);

						final String supportedFeature = testerExtension.getAttribute(TESTER_FEATURE);

						tester.add(new ECPStaticApplicableTester(singleValue, priority, supportedClassType,
							supportedEObject, supportedFeature));
					}
				}
				final ECPControlDescription controlDescription = new ECPControlDescription(id, resolvedClass,
					showLabel, tester);
				controlDescriptors.add(controlDescription);
			} catch (final ClassNotFoundException e1) {
				Activator.logException(e1);
			} catch (final CoreException e1) {
				Activator.logException(e1);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz) throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + EditMessages.CONTROLFACTROY_CANNOT_BE_LOADED
				+ bundleName
				+ EditMessages.CONTROLFACTORY_CANNOT_BE_RESOLVED);
		}
		//bundle.loadClass is used to load the class lying within the bundle
		return (Class<T>) bundle.loadClass(clazz);
	}
