package wk.foundation;

/**
 * A convenience class for easy construction of keypaths
 * when using those static KEY constants generated by eogenerator.
 * Saves a lot of typing blah + "." + blah ...
 *
 * @author kieran
 *
 */
public class KeyPathBuilder {
	private String _aKeyPath;

	/**
	 * @param aKey that is appended to the current keypath
	 * @return this KeyPathBuilder
	 */
	public KeyPathBuilder addKey(String aKey) {
		_aKeyPath = (_aKeyPath == null ? aKey : _aKeyPath + "." + aKey);
		return this;
	}

	@Override
	public String toString() {
		return _aKeyPath;
	}
}
