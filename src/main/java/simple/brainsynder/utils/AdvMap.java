/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package simple.brainsynder.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is my advanced version of a Map/HashMap
 *
 * {@link Map}
 */
@Deprecated
public class AdvMap <KeyType, ValueType> {
	private Map<KeyType, ValueType> KMap = new HashMap<> ();
	private Map<ValueType, KeyType> VMap = new HashMap<> ();

	public boolean containsKey(KeyType key)
	{
		return this.KMap.containsKey(key);
	}

	public boolean containsValue(ValueType key)
	{
		return this.KMap.containsValue (key);
	}

	public Set<Map.Entry<KeyType, ValueType>> entrySet()
	{
		return this.KMap.entrySet ();
	}

	public Set<KeyType> keySet()
	{
		return this.KMap.keySet ();
	}

	public Collection<ValueType> values()
	{
		return this.KMap.values ();
	}

	public ValueType getKey(KeyType key)
	{
		return this.KMap.get(key);
	}

	public KeyType getValue(ValueType key)
	{
		return this.VMap.get(key);
	}

	public void remove(KeyType key)
	{
		this.VMap.remove(getKey (key));
		this.KMap.remove(key);
	}

	public void put(KeyType key, ValueType value)
	{
		this.KMap.put(key, value);
		this.VMap.put(value, key);
	}

	public void clear()
	{
		this.KMap.clear();
		this.VMap.clear();
	}

	public int size()
	{
		return this.KMap.size();
	}

	public boolean isEmpty()
	{
		return this.KMap.isEmpty() | this.VMap.isEmpty ();
	}
}
