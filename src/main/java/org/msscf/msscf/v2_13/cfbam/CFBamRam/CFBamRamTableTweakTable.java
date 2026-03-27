
// Description: Java 11 in-memory RAM DbIO implementation for TableTweak.

/*
 *	org.msscf.msscf.CFBam
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal CFBam 2.13 Business Application Model
 *	
 *	Copyright 2016-2026 Mark Stephen Sobkow
 *	
 *	This file is part of Mark's Code Fractal CFBam.
 *	
 *	Mark's Code Fractal CFBam is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU General Public License,
 *	Version 3 or later with classpath and static linking exceptions.
 *	
 *	As a special exception, Mark Sobkow gives you permission to link this library
 *	with independent modules to produce an executable, provided that none of them
 *	conflict with the intent of the GPLv3; that is, you are not allowed to invoke
 *	the methods of this library from non-GPLv3-compatibly licensed code. You may not
 *	implement an LPGLv3 "wedge" to try to bypass this restriction. That said, code which
 *	does not rely on this library is free to specify whatever license its authors decide
 *	to use. Mark Sobkow specifically rejects the infectious nature of the GPLv3, and
 *	considers the mere act of including GPLv3 modules in an executable to be perfectly
 *	reasonable given tools like modern Java's single-jar deployment options.
 *	
 *	Mark's Code Fractal CFBam is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	Mark's Code Fractal CFBam is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with Mark's Code Fractal CFBam.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	If you wish to modify and use this code without publishing your changes,
 *	or integrate it with proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 *
 *	Manufactured by MSS Code Factory 2.12
 */

package org.msscf.msscf.v2_13.cfbam.CFBamRam;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.msscf.msscf.v2_13.cflib.CFLib.*;
import org.msscf.msscf.v2_13.cflib.CFLib.xml.*;
import org.msscf.msscf.v2_13.cfsec.CFSec.*;
import org.msscf.msscf.v2_13.cfint.CFInt.*;
import org.msscf.msscf.v2_13.cfbam.CFBam.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;
import org.msscf.msscf.v2_13.cfsec.CFSecObj.*;
import org.msscf.msscf.v2_13.cfint.CFIntObj.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamRamTableTweakTable in-memory RAM DbIO implementation
 *	for TableTweak.
 */
public class CFBamRamTableTweakTable
	implements ICFBamTableTweakTable
{
	private ICFBamSchema schema;
	private Map< CFBamTweakPKey,
				CFBamTableTweakBuff > dictByPKey
		= new HashMap< CFBamTweakPKey,
				CFBamTableTweakBuff >();
	private Map< CFBamTableTweakByTableIdxKey,
				Map< CFBamTweakPKey,
					CFBamTableTweakBuff >> dictByTableIdx
		= new HashMap< CFBamTableTweakByTableIdxKey,
				Map< CFBamTweakPKey,
					CFBamTableTweakBuff >>();

	public CFBamRamTableTweakTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createTableTweak( CFSecAuthorization Authorization,
		CFBamTableTweakBuff Buff )
	{
		final String S_ProcName = "createTableTweak";
		schema.getTableTweak().createTweak( Authorization,
			Buff );
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamTableTweakByTableIdxKey keyTableIdx = schema.getFactoryTableTweak().newTableIdxKey();
		keyTableIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTweak().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"Tweak",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamTweakPKey, CFBamTableTweakBuff > subdictTableIdx;
		if( dictByTableIdx.containsKey( keyTableIdx ) ) {
			subdictTableIdx = dictByTableIdx.get( keyTableIdx );
		}
		else {
			subdictTableIdx = new HashMap< CFBamTweakPKey, CFBamTableTweakBuff >();
			dictByTableIdx.put( keyTableIdx, subdictTableIdx );
		}
		subdictTableIdx.put( pkey, Buff );

	}

	public CFBamTableTweakBuff readDerived( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamTableTweak.readDerived";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamTableTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableTweakBuff lockDerived( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamTableTweak.readDerived";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamTableTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableTweakBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamTableTweak.readAllDerived";
		CFBamTableTweakBuff[] retList = new CFBamTableTweakBuff[ dictByPKey.values().size() ];
		Iterator< CFBamTableTweakBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamTableTweakBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByUNameIdx";
		CFBamTweakBuff buff = schema.getTableTweak().readDerivedByUNameIdx( Authorization,
			TenantId,
			ScopeId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof CFBamTableTweakBuff ) {
			return( (CFBamTableTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamTableTweakBuff[] readDerivedByValTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByValTentIdx";
		CFBamTweakBuff buffList[] = schema.getTableTweak().readDerivedByValTentIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamTweakBuff buff;
			ArrayList<CFBamTableTweakBuff> filteredList = new ArrayList<CFBamTableTweakBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamTableTweakBuff ) ) {
					filteredList.add( (CFBamTableTweakBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamTableTweakBuff[0] ) );
		}
	}

	public CFBamTableTweakBuff[] readDerivedByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByScopeIdx";
		CFBamTweakBuff buffList[] = schema.getTableTweak().readDerivedByScopeIdx( Authorization,
			TenantId,
			ScopeId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamTweakBuff buff;
			ArrayList<CFBamTableTweakBuff> filteredList = new ArrayList<CFBamTableTweakBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamTableTweakBuff ) ) {
					filteredList.add( (CFBamTableTweakBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamTableTweakBuff[0] ) );
		}
	}

	public CFBamTableTweakBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByDefSchemaIdx";
		CFBamTweakBuff buffList[] = schema.getTableTweak().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamTweakBuff buff;
			ArrayList<CFBamTableTweakBuff> filteredList = new ArrayList<CFBamTableTweakBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamTableTweakBuff ) ) {
					filteredList.add( (CFBamTableTweakBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamTableTweakBuff[0] ) );
		}
	}

	public CFBamTableTweakBuff[] readDerivedByTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId )
	{
		final String S_ProcName = "CFBamRamTableTweak.readDerivedByTableIdx";
		CFBamTableTweakByTableIdxKey key = schema.getFactoryTableTweak().newTableIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredTableId( TableId );

		CFBamTableTweakBuff[] recArray;
		if( dictByTableIdx.containsKey( key ) ) {
			Map< CFBamTweakPKey, CFBamTableTweakBuff > subdictTableIdx
				= dictByTableIdx.get( key );
			recArray = new CFBamTableTweakBuff[ subdictTableIdx.size() ];
			Iterator< CFBamTableTweakBuff > iter = subdictTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamTweakPKey, CFBamTableTweakBuff > subdictTableIdx
				= new HashMap< CFBamTweakPKey, CFBamTableTweakBuff >();
			dictByTableIdx.put( key, subdictTableIdx );
			recArray = new CFBamTableTweakBuff[0];
		}
		return( recArray );
	}

	public CFBamTableTweakBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByIdIdx() ";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamTableTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableTweakBuff readBuff( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamTableTweak.readBuff";
		CFBamTableTweakBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88b" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableTweakBuff lockBuff( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamTableTweakBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88b" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableTweakBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamTableTweak.readAllBuff";
		CFBamTableTweakBuff buff;
		ArrayList<CFBamTableTweakBuff> filteredList = new ArrayList<CFBamTableTweakBuff>();
		CFBamTableTweakBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88b" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamTableTweakBuff[0] ) );
	}

	public CFBamTableTweakBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByIdIdx() ";
		CFBamTableTweakBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
			return( (CFBamTableTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamTableTweakBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByUNameIdx() ";
		CFBamTableTweakBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			ScopeId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
			return( (CFBamTableTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamTableTweakBuff[] readBuffByValTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByValTentIdx() ";
		CFBamTableTweakBuff buff;
		ArrayList<CFBamTableTweakBuff> filteredList = new ArrayList<CFBamTableTweakBuff>();
		CFBamTableTweakBuff[] buffList = readDerivedByValTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( (CFBamTableTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableTweakBuff[0] ) );
	}

	public CFBamTableTweakBuff[] readBuffByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByScopeIdx() ";
		CFBamTableTweakBuff buff;
		ArrayList<CFBamTableTweakBuff> filteredList = new ArrayList<CFBamTableTweakBuff>();
		CFBamTableTweakBuff[] buffList = readDerivedByScopeIdx( Authorization,
			TenantId,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( (CFBamTableTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableTweakBuff[0] ) );
	}

	public CFBamTableTweakBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByDefSchemaIdx() ";
		CFBamTableTweakBuff buff;
		ArrayList<CFBamTableTweakBuff> filteredList = new ArrayList<CFBamTableTweakBuff>();
		CFBamTableTweakBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( (CFBamTableTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableTweakBuff[0] ) );
	}

	public CFBamTableTweakBuff[] readBuffByTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId )
	{
		final String S_ProcName = "CFBamRamTableTweak.readBuffByTableIdx() ";
		CFBamTableTweakBuff buff;
		ArrayList<CFBamTableTweakBuff> filteredList = new ArrayList<CFBamTableTweakBuff>();
		CFBamTableTweakBuff[] buffList = readDerivedByTableIdx( Authorization,
			TenantId,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88b" ) ) {
				filteredList.add( (CFBamTableTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableTweakBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific TableTweak buffer instances identified by the duplicate key TableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argTenantId	The TableTweak key attribute of the instance generating the id.
	 *
	 *	@param	argTableId	The TableTweak key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamTableTweakBuff[] pageBuffByTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateTableTweak( CFSecAuthorization Authorization,
		CFBamTableTweakBuff Buff )
	{
		schema.getTableTweak().updateTweak( Authorization,
			Buff );
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamTableTweakBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateTableTweak",
				"Existing record not found",
				"TableTweak",
				pkey );
		}
		CFBamTableTweakByTableIdxKey existingKeyTableIdx = schema.getFactoryTableTweak().newTableIdxKey();
		existingKeyTableIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamTableTweakByTableIdxKey newKeyTableIdx = schema.getFactoryTableTweak().newTableIdxKey();
		newKeyTableIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTweak().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTableTweak",
						"Superclass",
						"SuperClass",
						"Tweak",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTableTweak",
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamTweakPKey, CFBamTableTweakBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByTableIdx.get( existingKeyTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByTableIdx.containsKey( newKeyTableIdx ) ) {
			subdict = dictByTableIdx.get( newKeyTableIdx );
		}
		else {
			subdict = new HashMap< CFBamTweakPKey, CFBamTableTweakBuff >();
			dictByTableIdx.put( newKeyTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteTableTweak( CFSecAuthorization Authorization,
		CFBamTableTweakBuff Buff )
	{
		final String S_ProcName = "CFBamRamTableTweakTable.deleteTableTweak() ";
		String classCode;
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamTableTweakBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteTableTweak",
				pkey );
		}
		CFBamTableTweakByTableIdxKey keyTableIdx = schema.getFactoryTableTweak().newTableIdxKey();
		keyTableIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamTweakPKey, CFBamTableTweakBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByTableIdx.get( keyTableIdx );
		subdict.remove( pkey );

		schema.getTableTweak().deleteTweak( Authorization,
			Buff );
	}
	public void deleteTableTweakByTableIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argTableId )
	{
		CFBamTableTweakByTableIdxKey key = schema.getFactoryTableTweak().newTableIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredTableId( argTableId );
		deleteTableTweakByTableIdx( Authorization, key );
	}

	public void deleteTableTweakByTableIdx( CFSecAuthorization Authorization,
		CFBamTableTweakByTableIdxKey argKey )
	{
		CFBamTableTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableTweakBuff> matchSet = new LinkedList<CFBamTableTweakBuff>();
		Iterator<CFBamTableTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteTableTweak( Authorization, cur );
		}
	}

	public void deleteTableTweakByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteTableTweakByIdIdx( Authorization, key );
	}

	public void deleteTableTweakByIdIdx( CFSecAuthorization Authorization,
		CFBamTweakPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamTableTweakBuff cur;
		LinkedList<CFBamTableTweakBuff> matchSet = new LinkedList<CFBamTableTweakBuff>();
		Iterator<CFBamTableTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteTableTweak( Authorization, cur );
		}
	}

	public void deleteTableTweakByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId,
		String argName )
	{
		CFBamTweakByUNameIdxKey key = schema.getFactoryTweak().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteTableTweakByUNameIdx( Authorization, key );
	}

	public void deleteTableTweakByUNameIdx( CFSecAuthorization Authorization,
		CFBamTweakByUNameIdxKey argKey )
	{
		CFBamTableTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableTweakBuff> matchSet = new LinkedList<CFBamTableTweakBuff>();
		Iterator<CFBamTableTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteTableTweak( Authorization, cur );
		}
	}

	public void deleteTableTweakByValTentIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamTweakByValTentIdxKey key = schema.getFactoryTweak().newValTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteTableTweakByValTentIdx( Authorization, key );
	}

	public void deleteTableTweakByValTentIdx( CFSecAuthorization Authorization,
		CFBamTweakByValTentIdxKey argKey )
	{
		CFBamTableTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableTweakBuff> matchSet = new LinkedList<CFBamTableTweakBuff>();
		Iterator<CFBamTableTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteTableTweak( Authorization, cur );
		}
	}

	public void deleteTableTweakByScopeIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId )
	{
		CFBamTweakByScopeIdxKey key = schema.getFactoryTweak().newScopeIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		deleteTableTweakByScopeIdx( Authorization, key );
	}

	public void deleteTableTweakByScopeIdx( CFSecAuthorization Authorization,
		CFBamTweakByScopeIdxKey argKey )
	{
		CFBamTableTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableTweakBuff> matchSet = new LinkedList<CFBamTableTweakBuff>();
		Iterator<CFBamTableTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteTableTweak( Authorization, cur );
		}
	}

	public void deleteTableTweakByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamTweakByDefSchemaIdxKey key = schema.getFactoryTweak().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteTableTweakByDefSchemaIdx( Authorization, key );
	}

	public void deleteTableTweakByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamTweakByDefSchemaIdxKey argKey )
	{
		CFBamTableTweakBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableTweakBuff> matchSet = new LinkedList<CFBamTableTweakBuff>();
		Iterator<CFBamTableTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteTableTweak( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
