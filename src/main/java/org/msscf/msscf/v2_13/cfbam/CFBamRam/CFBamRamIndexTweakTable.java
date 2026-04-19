
// Description: Java 11 in-memory RAM DbIO implementation for IndexTweak.

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
 *	CFBamRamIndexTweakTable in-memory RAM DbIO implementation
 *	for IndexTweak.
 */
public class CFBamRamIndexTweakTable
	implements ICFBamIndexTweakTable
{
	private ICFBamSchema schema;
	private Map< CFBamTweakPKey,
				CFBamIndexTweakBuff > dictByPKey
		= new HashMap< CFBamTweakPKey,
				CFBamIndexTweakBuff >();
	private Map< CFBamIndexTweakByIndexIdxKey,
				Map< CFBamTweakPKey,
					CFBamIndexTweakBuff >> dictByIndexIdx
		= new HashMap< CFBamIndexTweakByIndexIdxKey,
				Map< CFBamTweakPKey,
					CFBamIndexTweakBuff >>();

	public CFBamRamIndexTweakTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createIndexTweak( CFSecAuthorization Authorization,
		CFBamIndexTweakBuff Buff )
	{
		final String S_ProcName = "createIndexTweak";
		schema.getTableTweak().createTweak( Authorization,
			Buff );
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexTweakByIndexIdxKey keyIndexIdx = schema.getFactoryIndexTweak().newIndexIdxKey();
		keyIndexIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

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
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Index",
						"Index",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamTweakPKey, CFBamIndexTweakBuff > subdictIndexIdx;
		if( dictByIndexIdx.containsKey( keyIndexIdx ) ) {
			subdictIndexIdx = dictByIndexIdx.get( keyIndexIdx );
		}
		else {
			subdictIndexIdx = new HashMap< CFBamTweakPKey, CFBamIndexTweakBuff >();
			dictByIndexIdx.put( keyIndexIdx, subdictIndexIdx );
		}
		subdictIndexIdx.put( pkey, Buff );

	}

	public CFBamIndexTweakBuff readDerived( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readDerived";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamIndexTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexTweakBuff lockDerived( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readDerived";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamIndexTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexTweakBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamIndexTweak.readAllDerived";
		CFBamIndexTweakBuff[] retList = new CFBamIndexTweakBuff[ dictByPKey.values().size() ];
		Iterator< CFBamIndexTweakBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamIndexTweakBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
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
		else if( buff instanceof CFBamIndexTweakBuff ) {
			return( (CFBamIndexTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexTweakBuff readDerivedByUDefIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByUDefIdx";
		CFBamTweakBuff buff = schema.getTableTweak().readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof CFBamIndexTweakBuff ) {
			return( (CFBamIndexTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexTweakBuff[] readDerivedByValTentIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamIndexTweakBuff> filteredList = new ArrayList<CFBamIndexTweakBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamIndexTweakBuff ) ) {
					filteredList.add( (CFBamIndexTweakBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamIndexTweakBuff[0] ) );
		}
	}

	public CFBamIndexTweakBuff[] readDerivedByScopeIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamIndexTweakBuff> filteredList = new ArrayList<CFBamIndexTweakBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamIndexTweakBuff ) ) {
					filteredList.add( (CFBamIndexTweakBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamIndexTweakBuff[0] ) );
		}
	}

	public CFBamIndexTweakBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamIndexTweakBuff> filteredList = new ArrayList<CFBamIndexTweakBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamIndexTweakBuff ) ) {
					filteredList.add( (CFBamIndexTweakBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamIndexTweakBuff[0] ) );
		}
	}

	public CFBamIndexTweakBuff[] readDerivedByIndexIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readDerivedByIndexIdx";
		CFBamIndexTweakByIndexIdxKey key = schema.getFactoryIndexTweak().newIndexIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredIndexId( IndexId );

		CFBamIndexTweakBuff[] recArray;
		if( dictByIndexIdx.containsKey( key ) ) {
			Map< CFBamTweakPKey, CFBamIndexTweakBuff > subdictIndexIdx
				= dictByIndexIdx.get( key );
			recArray = new CFBamIndexTweakBuff[ subdictIndexIdx.size() ];
			Iterator< CFBamIndexTweakBuff > iter = subdictIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamTweakPKey, CFBamIndexTweakBuff > subdictIndexIdx
				= new HashMap< CFBamTweakPKey, CFBamIndexTweakBuff >();
			dictByIndexIdx.put( key, subdictIndexIdx );
			recArray = new CFBamIndexTweakBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexTweakBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByIdIdx() ";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamIndexTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexTweakBuff readBuff( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readBuff";
		CFBamIndexTweakBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a80b" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexTweakBuff lockBuff( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamIndexTweakBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a80b" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexTweakBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readAllBuff";
		CFBamIndexTweakBuff buff;
		ArrayList<CFBamIndexTweakBuff> filteredList = new ArrayList<CFBamIndexTweakBuff>();
		CFBamIndexTweakBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80b" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexTweakBuff[0] ) );
	}

	public CFBamIndexTweakBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByIdIdx() ";
		CFBamIndexTweakBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
			return( (CFBamIndexTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexTweakBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByUNameIdx() ";
		CFBamIndexTweakBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			ScopeId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
			return( (CFBamIndexTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexTweakBuff readBuffByUDefIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByUDefIdx() ";
		CFBamIndexTweakBuff buff = readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
			return( (CFBamIndexTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexTweakBuff[] readBuffByValTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByValTentIdx() ";
		CFBamIndexTweakBuff buff;
		ArrayList<CFBamIndexTweakBuff> filteredList = new ArrayList<CFBamIndexTweakBuff>();
		CFBamIndexTweakBuff[] buffList = readDerivedByValTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( (CFBamIndexTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexTweakBuff[0] ) );
	}

	public CFBamIndexTweakBuff[] readBuffByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByScopeIdx() ";
		CFBamIndexTweakBuff buff;
		ArrayList<CFBamIndexTweakBuff> filteredList = new ArrayList<CFBamIndexTweakBuff>();
		CFBamIndexTweakBuff[] buffList = readDerivedByScopeIdx( Authorization,
			TenantId,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( (CFBamIndexTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexTweakBuff[0] ) );
	}

	public CFBamIndexTweakBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByDefSchemaIdx() ";
		CFBamIndexTweakBuff buff;
		ArrayList<CFBamIndexTweakBuff> filteredList = new ArrayList<CFBamIndexTweakBuff>();
		CFBamIndexTweakBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( (CFBamIndexTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexTweakBuff[0] ) );
	}

	public CFBamIndexTweakBuff[] readBuffByIndexIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readBuffByIndexIdx() ";
		CFBamIndexTweakBuff buff;
		ArrayList<CFBamIndexTweakBuff> filteredList = new ArrayList<CFBamIndexTweakBuff>();
		CFBamIndexTweakBuff[] buffList = readDerivedByIndexIdx( Authorization,
			TenantId,
			IndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80b" ) ) {
				filteredList.add( (CFBamIndexTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexTweakBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific IndexTweak buffer instances identified by the duplicate key IndexIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argTenantId	The IndexTweak key attribute of the instance generating the id.
	 *
	 *	@param	argIndexId	The IndexTweak key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamIndexTweakBuff[] pageBuffByIndexIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByIndexIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateIndexTweak( CFSecAuthorization Authorization,
		CFBamIndexTweakBuff Buff )
	{
		schema.getTableTweak().updateTweak( Authorization,
			Buff );
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexTweakBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateIndexTweak",
				"Existing record not found",
				"IndexTweak",
				pkey );
		}
		CFBamIndexTweakByIndexIdxKey existingKeyIndexIdx = schema.getFactoryIndexTweak().newIndexIdxKey();
		existingKeyIndexIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		CFBamIndexTweakByIndexIdxKey newKeyIndexIdx = schema.getFactoryIndexTweak().newIndexIdxKey();
		newKeyIndexIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

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
						"updateIndexTweak",
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
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexTweak",
						"Container",
						"Index",
						"Index",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamTweakPKey, CFBamIndexTweakBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByIndexIdx.get( existingKeyIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIndexIdx.containsKey( newKeyIndexIdx ) ) {
			subdict = dictByIndexIdx.get( newKeyIndexIdx );
		}
		else {
			subdict = new HashMap< CFBamTweakPKey, CFBamIndexTweakBuff >();
			dictByIndexIdx.put( newKeyIndexIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteIndexTweak( CFSecAuthorization Authorization,
		CFBamIndexTweakBuff Buff )
	{
		final String S_ProcName = "CFBamRamIndexTweakTable.deleteIndexTweak() ";
		String classCode;
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexTweakBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteIndexTweak",
				pkey );
		}
		CFBamIndexTweakByIndexIdxKey keyIndexIdx = schema.getFactoryIndexTweak().newIndexIdxKey();
		keyIndexIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamTweakPKey, CFBamIndexTweakBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByIndexIdx.get( keyIndexIdx );
		subdict.remove( pkey );

		schema.getTableTweak().deleteTweak( Authorization,
			Buff );
	}
	public void deleteIndexTweakByIndexIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argIndexId )
	{
		CFBamIndexTweakByIndexIdxKey key = schema.getFactoryIndexTweak().newIndexIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredIndexId( argIndexId );
		deleteIndexTweakByIndexIdx( Authorization, key );
	}

	public void deleteIndexTweakByIndexIdx( CFSecAuthorization Authorization,
		CFBamIndexTweakByIndexIdxKey argKey )
	{
		CFBamIndexTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexTweakBuff> matchSet = new LinkedList<CFBamIndexTweakBuff>();
		Iterator<CFBamIndexTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexTweak( Authorization, cur );
		}
	}

	public void deleteIndexTweakByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteIndexTweakByIdIdx( Authorization, key );
	}

	public void deleteIndexTweakByIdIdx( CFSecAuthorization Authorization,
		CFBamTweakPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamIndexTweakBuff cur;
		LinkedList<CFBamIndexTweakBuff> matchSet = new LinkedList<CFBamIndexTweakBuff>();
		Iterator<CFBamIndexTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexTweak( Authorization, cur );
		}
	}

	public void deleteIndexTweakByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId,
		String argName )
	{
		CFBamTweakByUNameIdxKey key = schema.getFactoryTweak().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteIndexTweakByUNameIdx( Authorization, key );
	}

	public void deleteIndexTweakByUNameIdx( CFSecAuthorization Authorization,
		CFBamTweakByUNameIdxKey argKey )
	{
		CFBamIndexTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexTweakBuff> matchSet = new LinkedList<CFBamIndexTweakBuff>();
		Iterator<CFBamIndexTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexTweak( Authorization, cur );
		}
	}

	public void deleteIndexTweakByUDefIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId,
		Long argDefSchemaTenantId,
		Long argDefSchemaId,
		String argName )
	{
		CFBamTweakByUDefIdxKey key = schema.getFactoryTweak().newUDefIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		key.setRequiredName( argName );
		deleteIndexTweakByUDefIdx( Authorization, key );
	}

	public void deleteIndexTweakByUDefIdx( CFSecAuthorization Authorization,
		CFBamTweakByUDefIdxKey argKey )
	{
		CFBamIndexTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( argKey.getOptionalDefSchemaTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexTweakBuff> matchSet = new LinkedList<CFBamIndexTweakBuff>();
		Iterator<CFBamIndexTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexTweak( Authorization, cur );
		}
	}

	public void deleteIndexTweakByValTentIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamTweakByValTentIdxKey key = schema.getFactoryTweak().newValTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteIndexTweakByValTentIdx( Authorization, key );
	}

	public void deleteIndexTweakByValTentIdx( CFSecAuthorization Authorization,
		CFBamTweakByValTentIdxKey argKey )
	{
		CFBamIndexTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexTweakBuff> matchSet = new LinkedList<CFBamIndexTweakBuff>();
		Iterator<CFBamIndexTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexTweak( Authorization, cur );
		}
	}

	public void deleteIndexTweakByScopeIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId )
	{
		CFBamTweakByScopeIdxKey key = schema.getFactoryTweak().newScopeIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		deleteIndexTweakByScopeIdx( Authorization, key );
	}

	public void deleteIndexTweakByScopeIdx( CFSecAuthorization Authorization,
		CFBamTweakByScopeIdxKey argKey )
	{
		CFBamIndexTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexTweakBuff> matchSet = new LinkedList<CFBamIndexTweakBuff>();
		Iterator<CFBamIndexTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexTweak( Authorization, cur );
		}
	}

	public void deleteIndexTweakByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamTweakByDefSchemaIdxKey key = schema.getFactoryTweak().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteIndexTweakByDefSchemaIdx( Authorization, key );
	}

	public void deleteIndexTweakByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamTweakByDefSchemaIdxKey argKey )
	{
		CFBamIndexTweakBuff cur;
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
		LinkedList<CFBamIndexTweakBuff> matchSet = new LinkedList<CFBamIndexTweakBuff>();
		Iterator<CFBamIndexTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexTweak( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
