package wk.prototypes;

//
//EncodedString.java
//

import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.crypting.ERXCrypto;

/**
 * A class to handle the management of sensitive information. It depends on
 * Project Wonder's ERXCrypto class in ERExtensions framework.
 * Anyway the nice thing is that it
 * allows encoding and decoding on the fly in and out of the database by
 * creating a custom type in EOModeler using <br />
 * <br />
 *
 * Class = EncodedString<br />
 * Factory Method = createInstanceFromEncodedString:<br />
 * Conversion Method = toEncodedString<br />
 * Init Argument = NSString <br />
 * <br />
 *
 * It also has a toMaskedString method for convenient masking of the sensitive
 * string in WOComponents. By default toString returns toMaskedString.
 *
 * Like java.lang.String, EncodedString is immutable and cannot be changed once
 * created so an instance can be shared among different EOs when copying for
 * example.
 */
public class EncodedString implements NSKeyValueCoding {

	private final String _decodedValue;
	private String _encodedValue;
	private int _leadingCharCount = -1;
	private int _trailingCharCount = -1;

	/** Initializes with the decoded value set to an empty String */
	public EncodedString() {
		_decodedValue = "";
	}

	/** Initializes this class with an original unencrypted value */
	public EncodedString(String decodedValue) {
		_decodedValue = decodedValue;
	}

	/**
	 * @return an instance of this class from an encoded string. Typically this
	 *         factory method is used to create instances of this class from
	 *         encoded value from the database.
	 */
	public static EncodedString createInstanceFromEncodedString(
			String encodedString) {
		return (encodedString == null ? null : new EncodedString(
				blowfishDecode(encodedString)));
	}

	/** @return a masked string
	 * TODO : Move this into a StringUtilities class */
	public static String maskedString(String stringToBeMasked) {
		return (new EncodedString(stringToBeMasked)).toMaskedString();
	}

	/**
	 * @return blowfish encoded output of the value wrapped by this class.
	 *         Typically this method generates the sensitive value that is
	 *         stored "securely" in the database.
	 */
	public String toEncodedString() {
		if (_encodedValue == null && _decodedValue != null) {
			_encodedValue = blowfishEncode(_decodedValue);
		} //~ if (_encodedValue == null && _decodedValue != null)
		return _encodedValue;
	}

	/** @return the naked value */
	public String value() {
		return _decodedValue;
	}

	/** @return toMaskedString(). This is for key value coding. */
	public String formValue() {
		return toMaskedString();
	}

	/**
	 * @return a "masked" string where middle characters of the string are
	 *         replaced by asterisks
	 */
	public String toMaskedString() {
		// It should not be null, but I'll check anyway
		return (_decodedValue == null ? "" : overlay(_decodedValue, repeat("*",
				_decodedValue.length() - leadingCharCount()
						- trailingCharCount()), leadingCharCount(),
				_decodedValue.length() - trailingCharCount()));

	}

	/** @return string representation of this string - the masked string */
	@Override
	public String toString() {
		return toMaskedString();
	}

	/**
	 * @return the number of characters at the beginning of the string that
	 *         should not be masked when displaying. This can be made more
	 *         elaborate later when I have time. The value can be calculated
	 *         based on the length of the string
	 */
	public int leadingCharCount() {
		if (_leadingCharCount < 0) {
			// We automatically calculate it
			if (_decodedValue == null) {
				return 0;
			} else {
				if (_decodedValue.length() < 10) {
					return 0;
				} else if (_decodedValue.length() < 21) {
					return 1;
				} else {
					return 2;
				}
			}
		}

		return _leadingCharCount;
	}

	public void setLeadingCharCount(int newLeadingCharCount) {
		_leadingCharCount = newLeadingCharCount;
	}

	/**
	 * @return the number of characters at the end of the string that should not
	 *         be masked when displaying. This can be made more elaborate later
	 *         when I have time. The value can be calculated based on the length
	 *         of the string
	 */
	public int trailingCharCount() {
		if (_trailingCharCount < 0) {
			// Calculate it
			if (_decodedValue == null) {
				return 0;
			} else {
				// Note we do not use break statements since we are using return
				// to get out of the switch block
				switch (_decodedValue.length()) {
				case 1:
				case 2:
					return 0;
				case 3:
				case 4:
				case 5:
				case 6:
					return 1;
				case 7:
				case 8:
					return 2;
				case 9:
				case 10:
				case 11:
				case 12:
					return 3;
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
					return 4;
				default:
					return 5;
				}
			}
		}

		return _trailingCharCount;
	}

	public void setTrailingCharCount(int newTrailingCharCount) {
		_trailingCharCount = newTrailingCharCount;
	}

	/** Implements NSKeyValueCoding interface */
	public Object valueForKey(String key) {
		return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
	}

	/** Implements NSKeyValueCoding interface */
	public void takeValueForKey(Object value, String key) {
		NSKeyValueCoding.DefaultImplementation
				.takeValueForKey(this, value, key);
	}

	private static String blowfishEncode(String clearText) {
		return ERXCrypto.crypterForAlgorithm(ERXCrypto.BLOWFISH).encrypt(
				clearText);
	}

	private static String blowfishDecode(String encryptedText) {
		return ERXCrypto.crypterForAlgorithm(ERXCrypto.BLOWFISH).decrypt(
				encryptedText);
	}

	// *********************************************************************************************************
	// The code below this line was
	// lifted from package org.apache.commons.lang and made private to reduce
	// dependencies of this proto class

	/**
	 * <p>
	 * Overlays part of a String with another String.
	 * </p>
	 *
	 * <p>
	 * A <code>null</code> string input returns <code>null</code>. A
	 * negative index is treated as zero. An index greater than the string
	 * length is treated as the string length. The start index is always the
	 * smaller of the two indices.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.overlay(null, *, *, *)            = null
	 * StringUtils.overlay(&quot;&quot;, &quot;abc&quot;, 0, 0)          = &quot;abc&quot;
	 * StringUtils.overlay(&quot;abcdef&quot;, null, 2, 4)     = &quot;abef&quot;
	 * StringUtils.overlay(&quot;abcdef&quot;, &quot;&quot;, 2, 4)       = &quot;abef&quot;
	 * StringUtils.overlay(&quot;abcdef&quot;, &quot;&quot;, 4, 2)       = &quot;abef&quot;
	 * StringUtils.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 2, 4)   = &quot;abzzzzef&quot;
	 * StringUtils.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 4, 2)   = &quot;abzzzzef&quot;
	 * StringUtils.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, -1, 4)  = &quot;zzzzef&quot;
	 * StringUtils.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 2, 8)   = &quot;abzzzz&quot;
	 * StringUtils.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, -2, -3) = &quot;zzzzabcdef&quot;
	 * StringUtils.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 8, 10)  = &quot;abcdefzzzz&quot;
	 * </pre>
	 *
	 * @param str
	 *            the String to do overlaying in, may be null
	 * @param overlay
	 *            the String to overlay, may be null
	 * @param start
	 *            the position to start overlaying at
	 * @param end
	 *            the position to stop overlaying before
	 * @return overlayed String, <code>null</code> if null String input
	 * @since 2.0
	 */
	private static String overlay(String str, String overlay, int start, int end) {
		if (str == null) {
			return null;
		}
		if (overlay == null) {
			overlay = EMPTY;
		}
		int len = str.length();
		if (start < 0) {
			start = 0;
		}
		if (start > len) {
			start = len;
		}
		if (end < 0) {
			end = 0;
		}
		if (end > len) {
			end = len;
		}
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		return new StringBuffer(len + start - end + overlay.length() + 1)
				.append(str.substring(0, start)).append(overlay).append(
						str.substring(end)).toString();
	}

	/**
	 * The empty String <code>""</code>.
	 *
	 * @since 2.0
	 */
	private static final String EMPTY = "";

	/**
	 * <p>
	 * Repeat a String <code>repeat</code> times to form a new String.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.repeat(null, 2) = null
	 * StringUtils.repeat(&quot;&quot;, 0)   = &quot;&quot;
	 * StringUtils.repeat(&quot;&quot;, 2)   = &quot;&quot;
	 * StringUtils.repeat(&quot;a&quot;, 3)  = &quot;aaa&quot;
	 * StringUtils.repeat(&quot;ab&quot;, 2) = &quot;abab&quot;
	 * StringUtils.repeat(&quot;a&quot;, -2) = &quot;&quot;
	 * </pre>
	 *
	 * @param str
	 *            the String to repeat, may be null
	 * @param repeat
	 *            number of times to repeat str, negative treated as zero
	 * @return a new String consisting of the original String repeated,
	 *         <code>null</code> if null String input
	 */
	private static String repeat(String str, int repeat) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		if (repeat <= 0) {
			return EMPTY;
		}
		int inputLength = str.length();
		if (repeat == 1 || inputLength == 0) {
			return str;
		}
		if (inputLength == 1 && repeat <= PAD_LIMIT) {
			return padding(repeat, str.charAt(0));
		}

		int outputLength = inputLength * repeat;
		switch (inputLength) {
		case 1:
			char ch = str.charAt(0);
			char[] output1 = new char[outputLength];
			for (int i = repeat - 1; i >= 0; i--) {
				output1[i] = ch;
			}
			return new String(output1);
		case 2:
			char ch0 = str.charAt(0);
			char ch1 = str.charAt(1);
			char[] output2 = new char[outputLength];
			for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
				output2[i] = ch0;
				output2[i + 1] = ch1;
			}
			return new String(output2);
		default:
			StringBuffer buf = new StringBuffer(outputLength);
			for (int i = 0; i < repeat; i++) {
				buf.append(str);
			}
			return buf.toString();
		}
	}

	/**
	 * <p>
	 * The maximum size to which the padding constant(s) can expand.
	 * </p>
	 */
	private static final int PAD_LIMIT = 8192;

	/**
	 * <p>
	 * Returns padding using the specified delimiter repeated to a given length.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.padding(0, 'e')  = &quot;&quot;
	 * StringUtils.padding(3, 'e')  = &quot;eee&quot;
	 * StringUtils.padding(-2, 'e') = IndexOutOfBoundsException
	 * </pre>
	 *
	 * <p>
	 * Note: this method doesn't not support padding with <a
	 * href="http://www.unicode.org/glossary/#supplementary_character">Unicode
	 * Supplementary Characters</a> as they require a pair of <code>char</code>s
	 * to be represented. If you are needing to support full I18N of your
	 * applications consider using {@link #repeat(String, int)} instead.
	 * </p>
	 *
	 * @param repeat
	 *            number of times to repeat delim
	 * @param padChar
	 *            character to repeat
	 * @return String with repeated character
	 * @throws IndexOutOfBoundsException
	 *             if <code>repeat &lt; 0</code>
	 * @see #repeat(String, int)
	 */
	private static String padding(int repeat, char padChar)
			throws IndexOutOfBoundsException {
		if (repeat < 0) {
			throw new IndexOutOfBoundsException(
					"Cannot pad a negative amount: " + repeat);
		}
		final char[] buf = new char[repeat];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = padChar;
		}
		return new String(buf);
	}

}
