package com.cqi.hr.util;

/**
 * 將StringBuilder封裝起來獲得額外的方法。
 * @version 1.0.1
 * @author Tim Wang<br/>
 * <br/>
 * History<br/>
 * [1.0.1]<br/>
 * add replace(String regex,String replace)<br/>
 * add replaceAll(String regex,String replace)<br/>
 */
public class StringBuilderExt implements Appendable, CharSequence {
	private StringBuilder _base;
	private int length = 0;

	public StringBuilderExt() {
		_base = new StringBuilder();
	}
	public StringBuilderExt(StringBuilder base) {
		if(base==null) base = new StringBuilder();
		_base = base;
	}
	public StringBuilderExt(String base) {
		if(base==null)base="";
		_base = new StringBuilder(base);
	}
	
	public StringBuilderExt appendAll(Object...messages) {
		if(messages!=null) {
			for(Object message:messages) {
				_base.append(message);
			}
		}
		return this;
	}
	
	public StringBuilderExt append0n(int n, int length) {
		if(length<=1) length = 0;
		switch (length) {
		case 0:
			_base.append(n);
			break;
		case 2:
			if(n<10) _base.append(0);
			_base.append(n);
			break;
		default:
			String s = String.valueOf(n);
			length = length - s.length();
			for(int i=0;i<length;++i) {
				_base.append(0);
			}
			_base.append(n);
			break;
		}
		
		return this;
	}
	
	public StringBuilderExt append0n(int n) {
		return append0n(n, 2);
	}
	
	public StringBuilderExt clear() {
		_base.setLength(0);
		return this;
	}
	
	public StringBuilderExt ltrim() {
		int firstIndex = 0;
		for(int i=0,len=_base.length();i<len;++i) {
			if(_base.charAt(i)>' ') {
				break;
			}
			firstIndex = i;
		}
		if(firstIndex>0) {
			_base.delete(0, firstIndex+1);
		}
		return this;
	}
	
	public StringBuilderExt rtrim() {
		final int LASE_INDEX = _base.length()-1;
		int lastIndex = LASE_INDEX;
		for(int i=LASE_INDEX;i>=0;--i) {
			if(_base.charAt(i)>' ') {
				break;
			}
			lastIndex = i;
		}
		if(lastIndex<LASE_INDEX) {
			_base.delete(lastIndex, LASE_INDEX+1);
		}
		return this;
	}
	
	public StringBuilderExt trim() {
		return ltrim().rtrim();
	}
	
	public String substr(int startIndex, int length) {
		if(startIndex<0 || length<=0) return "";
		
		int endIndex = startIndex+length;
		if(endIndex>_base.length()) endIndex = _base.length();
		
		return _base.substring(startIndex, endIndex);
	}
	
	public StringBuilderExt saveLength() {
		length = _base.length();
		return this;
	}
	
	public StringBuilderExt restoreLength() {
		_base.setLength(length);
		return this;
	}
	
	public static void main(String[] args) {
		StringBuilderExt sb = new StringBuilderExt();
		sb.append("PREFIX_").saveLength();
		sb.append0n(168,2);
		System.out.println(sb);
		sb.restoreLength().append0n(450,5);
		System.out.println(sb);
	}
	
	/**
	 * 取得基底StringBuilder。
	 * @return
	 */
	public StringBuilder getBase() {
		return _base;
	}
	
	// =======================================================
	// 以下為原始方法
	// =======================================================
	@Override
	public int length() {
		return _base.length();
	}

	public int capacity() {
		return _base.capacity();
	}

	@Override
	public int hashCode() {
		return _base.hashCode();
	}

	public void ensureCapacity(int minimumCapacity) {
		_base.ensureCapacity(minimumCapacity);
	}

	public void trimToSize() {
		_base.trimToSize();
	}

	public void setLength(int newLength) {
		_base.setLength(newLength);
	}

	@Override
	public boolean equals(Object obj) {
		return _base.equals(obj);
	}

	public StringBuilderExt append(Object obj) {
		_base.append(obj);
		return this;
	}

	public StringBuilderExt append(String str) {
		_base.append(str);
		return this;
	}

	public StringBuilderExt append(StringBuffer sb) {
		_base.append(sb);
		return this;
	}

	@Override
	public char charAt(int index) {
		return _base.charAt(index);
	}

	@Override
	public StringBuilderExt append(CharSequence s) {
		_base.append(s);
		return this;
	}

	public int codePointAt(int index) {
		return _base.codePointAt(index);
	}

	@Override
	public StringBuilderExt append(CharSequence s, int start, int end) {
		_base.append(s, start, end);
		return this;
	}

	public StringBuilderExt append(char[] str) {
		_base.append(str);
		return this;
	}

	public StringBuilderExt append(char[] str, int offset, int len) {
		_base.append(str, offset, len);
		return this;
	}

	public StringBuilderExt append(boolean b) {
		_base.append(b);
		return this;
	}

	@Override
	public StringBuilderExt append(char c) {
		_base.append(c);
		return this;
	}

	public StringBuilderExt append(int i) {
		_base.append(i);
		return this;
	}

	public int codePointBefore(int index) {
		return _base.codePointBefore(index);
	}

	public StringBuilderExt append(long lng) {
		_base.append(lng);
		return this;
	}

	public StringBuilderExt append(float f) {
		_base.append(f);
		return this;
	}

	public StringBuilderExt append(double d) {
		_base.append(d);
		return this;
	}

	public StringBuilderExt appendCodePoint(int codePoint) {
		_base.appendCodePoint(codePoint);
		return this;
	}

	public StringBuilderExt delete(int start, int end) {
		_base.delete(start, end);
		return this;
	}

	public StringBuilderExt deleteCharAt(int index) {
		_base.deleteCharAt(index);
		return this;
	}

	public StringBuilderExt replace(int start, int end, String str) {
		_base.replace(start, end, str);
		return this;
	}

	public int codePointCount(int beginIndex, int endIndex) {
		return _base.codePointCount(beginIndex, endIndex);
	}

	public StringBuilderExt insert(int index, char[] str, int offset, int len) {
		_base.insert(index, str, offset, len);
		return this;
	}

	public StringBuilderExt insert(int offset, Object obj) {
		_base.insert(offset, obj);
		return this;
	}

	public StringBuilderExt insert(int offset, String str) {
		_base.insert(offset, str);
		return this;
	}

	public StringBuilderExt insert(int offset, char[] str) {
		_base.insert(offset, str);
		return this;
	}

	public StringBuilderExt insert(int dstOffset, CharSequence s) {
		_base.insert(dstOffset, s);
		return this;
	}

	public int offsetByCodePoints(int index, int codePointOffset) {
		return _base.offsetByCodePoints(index, codePointOffset);
	}

	public StringBuilderExt insert(int dstOffset, CharSequence s, int start, int end) {
		_base.insert(dstOffset, s, start, end);
		return this;
	}

	public StringBuilderExt insert(int offset, boolean b) {
		_base.insert(offset, b);
		return this;
	}

	public StringBuilderExt insert(int offset, char c) {
		_base.insert(offset, c);
		return this;
	}

	public StringBuilderExt insert(int offset, int i) {
		_base.insert(offset, i);
		return this;
	}

	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
		_base.getChars(srcBegin, srcEnd, dst, dstBegin);
	}

	public StringBuilderExt insert(int offset, long l) {
		_base.insert(offset, l);
		return this;
	}

	public StringBuilderExt insert(int offset, float f) {
		_base.insert(offset, f);
		return this;
	}

	public StringBuilderExt insert(int offset, double d) {
		_base.insert(offset, d);
		return this;
	}

	public int indexOf(String str) {
		return _base.indexOf(str);
	}

	public int indexOf(String str, int fromIndex) {
		return _base.indexOf(str, fromIndex);
	}

	public int lastIndexOf(String str) {
		return _base.lastIndexOf(str);
	}

	public int lastIndexOf(String str, int fromIndex) {
		return _base.lastIndexOf(str, fromIndex);
	}

	public StringBuilderExt reverse() {
		_base.reverse();
		return this;
	}

	@Override
	public String toString() {
		return _base.toString();
	}

	public void setCharAt(int index, char ch) {
		_base.setCharAt(index, ch);
	}

	public String substring(int start) {
		return _base.substring(start);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return _base.subSequence(start, end);
	}

	public String substring(int start, int end) {
		return _base.substring(start, end);
	}
	
	public StringBuilderExt replace(String regex,String replace) {
		replace(_base,regex,replace);
		return this;
	}
	
	public StringBuilderExt replaceAll(String regex,String replace) {
		replaceAll(_base,regex,replace);
		return this;
	}
	
	private static StringBuilder replace(StringBuilder sb,String regex,String replace){
		int index = sb.indexOf(regex);
		if(index != -1){
			return sb.replace(index,index + regex.length(), replace);
		}else{
			return sb;
		}
	}
	
	private static StringBuilder replaceAll(StringBuilder sb,String regex,String replace){
		while(sb.indexOf(regex) != -1){
			sb = replace(sb, regex, replace);
		}
		return sb;
	}
	
	public StringBuilderExt remove(String beginStr,String endStr){
		int indexBegin = this.indexOf(beginStr);
		int indexEnd = this.indexOf(endStr) ;
		if(indexBegin != -1 && indexEnd != -1){
			this.replace(indexBegin, indexEnd + 1, "");
		}
		return this;
	}
}
