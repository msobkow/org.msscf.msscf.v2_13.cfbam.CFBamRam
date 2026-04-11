
// Description: Java 11 in-memory RAM DbIO implementation for SchemaTweak.

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
 *	CFBamRamSchemaTweakTable in-memory RAM DbIO implementation
 *	for SchemaTweak.
 */
public class CFBamRamSchemaTweakTable
	implements ICFBamSchemaTweakTable
{
	private ICFBamSchema schema;
	private Map< CFBamTweakPKey,
				CFBamSchemaTweakBuff > dictByPKey
		= new HashMap< CFBamTweakPKey,
				CFBamSchemaTweakBuff >();
	private Map< CFBamSchemaTweakBySchemaIdxKey,
				Map< CFBamTweakPKey,
					CFBamSchemaTweakBuff >> dictBySchemaIdx
		= new HashMap< CFBamSchemaTweakBySchemaIdxKey,
				Map< CFBamTweakPKey,
					CFBamSchemaTweakBuff >>();

	public CFBamRamSchemaTweakTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createSchemaTweak( CFSecAuthorization Authorization,
		CFBamSchemaTweakBuff Buff )
	{
		final String S_ProcName = "createSchemaTweak";
		schema.getTableTweak().createTweak( Authorization,
			Buff );
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaTweakBySchemaIdxKey keySchemaIdx = schema.getFactorySchemaTweak().newSchemaIdxKey();
		keySchemaIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Schema",
						"SchemaDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamTweakPKey, CFBamSchemaTweakBuff > subdictSchemaIdx;
		if( dictBySchemaIdx.containsKey( keySchemaIdx ) ) {
			subdictSchemaIdx = dictBySchemaIdx.get( keySchemaIdx );
		}
		else {
			subdictSchemaIdx = new HashMap< CFBamTweakPKey, CFBamSchemaTweakBuff >();
			dictBySchemaIdx.put( keySchemaIdx, subdictSchemaIdx );
		}
		subdictSchemaIdx.put( pkey, Buff );

	}

	public CFBamSchemaTweakBuff readDerived( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readDerived";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaTweakBuff lockDerived( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readDerived";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaTweakBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamSchemaTweak.readAllDerived";
		CFBamSchemaTweakBuff[] retList = new CFBamSchemaTweakBuff[ dictByPKey.values().size() ];
		Iterator< CFBamSchemaTweakBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamSchemaTweakBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
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
		else if( buff instanceof CFBamSchemaTweakBuff ) {
			return( (CFBamSchemaTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaTweakBuff readDerivedByUDefIdx( CFSecAuthorization Authorization,
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
		else if( buff instanceof CFBamSchemaTweakBuff ) {
			return( (CFBamSchemaTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaTweakBuff[] readDerivedByValTentIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamSchemaTweakBuff> filteredList = new ArrayList<CFBamSchemaTweakBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamSchemaTweakBuff ) ) {
					filteredList.add( (CFBamSchemaTweakBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamSchemaTweakBuff[0] ) );
		}
	}

	public CFBamSchemaTweakBuff[] readDerivedByScopeIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamSchemaTweakBuff> filteredList = new ArrayList<CFBamSchemaTweakBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamSchemaTweakBuff ) ) {
					filteredList.add( (CFBamSchemaTweakBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamSchemaTweakBuff[0] ) );
		}
	}

	public CFBamSchemaTweakBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamSchemaTweakBuff> filteredList = new ArrayList<CFBamSchemaTweakBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamSchemaTweakBuff ) ) {
					filteredList.add( (CFBamSchemaTweakBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamSchemaTweakBuff[0] ) );
		}
	}

	public CFBamSchemaTweakBuff[] readDerivedBySchemaIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaDefId )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readDerivedBySchemaIdx";
		CFBamSchemaTweakBySchemaIdxKey key = schema.getFactorySchemaTweak().newSchemaIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredSchemaDefId( SchemaDefId );

		CFBamSchemaTweakBuff[] recArray;
		if( dictBySchemaIdx.containsKey( key ) ) {
			Map< CFBamTweakPKey, CFBamSchemaTweakBuff > subdictSchemaIdx
				= dictBySchemaIdx.get( key );
			recArray = new CFBamSchemaTweakBuff[ subdictSchemaIdx.size() ];
			Iterator< CFBamSchemaTweakBuff > iter = subdictSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamTweakPKey, CFBamSchemaTweakBuff > subdictSchemaIdx
				= new HashMap< CFBamTweakPKey, CFBamSchemaTweakBuff >();
			dictBySchemaIdx.put( key, subdictSchemaIdx );
			recArray = new CFBamSchemaTweakBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaTweakBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByIdIdx() ";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamSchemaTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaTweakBuff readBuff( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readBuff";
		CFBamSchemaTweakBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88c" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaTweakBuff lockBuff( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamSchemaTweakBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88c" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaTweakBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readAllBuff";
		CFBamSchemaTweakBuff buff;
		ArrayList<CFBamSchemaTweakBuff> filteredList = new ArrayList<CFBamSchemaTweakBuff>();
		CFBamSchemaTweakBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88c" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaTweakBuff[0] ) );
	}

	public CFBamSchemaTweakBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByIdIdx() ";
		CFBamSchemaTweakBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
			return( (CFBamSchemaTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaTweakBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByUNameIdx() ";
		CFBamSchemaTweakBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			ScopeId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
			return( (CFBamSchemaTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaTweakBuff readBuffByUDefIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByUDefIdx() ";
		CFBamSchemaTweakBuff buff = readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
			return( (CFBamSchemaTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaTweakBuff[] readBuffByValTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByValTentIdx() ";
		CFBamSchemaTweakBuff buff;
		ArrayList<CFBamSchemaTweakBuff> filteredList = new ArrayList<CFBamSchemaTweakBuff>();
		CFBamSchemaTweakBuff[] buffList = readDerivedByValTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( (CFBamSchemaTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaTweakBuff[0] ) );
	}

	public CFBamSchemaTweakBuff[] readBuffByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByScopeIdx() ";
		CFBamSchemaTweakBuff buff;
		ArrayList<CFBamSchemaTweakBuff> filteredList = new ArrayList<CFBamSchemaTweakBuff>();
		CFBamSchemaTweakBuff[] buffList = readDerivedByScopeIdx( Authorization,
			TenantId,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( (CFBamSchemaTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaTweakBuff[0] ) );
	}

	public CFBamSchemaTweakBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByDefSchemaIdx() ";
		CFBamSchemaTweakBuff buff;
		ArrayList<CFBamSchemaTweakBuff> filteredList = new ArrayList<CFBamSchemaTweakBuff>();
		CFBamSchemaTweakBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( (CFBamSchemaTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaTweakBuff[0] ) );
	}

	public CFBamSchemaTweakBuff[] readBuffBySchemaIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaDefId )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readBuffBySchemaIdx() ";
		CFBamSchemaTweakBuff buff;
		ArrayList<CFBamSchemaTweakBuff> filteredList = new ArrayList<CFBamSchemaTweakBuff>();
		CFBamSchemaTweakBuff[] buffList = readDerivedBySchemaIdx( Authorization,
			TenantId,
			SchemaDefId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88c" ) ) {
				filteredList.add( (CFBamSchemaTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaTweakBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific SchemaTweak buffer instances identified by the duplicate key SchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argTenantId	The SchemaTweak key attribute of the instance generating the id.
	 *
	 *	@param	argSchemaDefId	The SchemaTweak key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaTweakBuff[] pageBuffBySchemaIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaDefId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffBySchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateSchemaTweak( CFSecAuthorization Authorization,
		CFBamSchemaTweakBuff Buff )
	{
		schema.getTableTweak().updateTweak( Authorization,
			Buff );
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaTweakBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateSchemaTweak",
				"Existing record not found",
				"SchemaTweak",
				pkey );
		}
		CFBamSchemaTweakBySchemaIdxKey existingKeySchemaIdx = schema.getFactorySchemaTweak().newSchemaIdxKey();
		existingKeySchemaIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamSchemaTweakBySchemaIdxKey newKeySchemaIdx = schema.getFactorySchemaTweak().newSchemaIdxKey();
		newKeySchemaIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

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
						"updateSchemaTweak",
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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaTweak",
						"Container",
						"Schema",
						"SchemaDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamTweakPKey, CFBamSchemaTweakBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictBySchemaIdx.get( existingKeySchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchemaIdx.containsKey( newKeySchemaIdx ) ) {
			subdict = dictBySchemaIdx.get( newKeySchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamTweakPKey, CFBamSchemaTweakBuff >();
			dictBySchemaIdx.put( newKeySchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteSchemaTweak( CFSecAuthorization Authorization,
		CFBamSchemaTweakBuff Buff )
	{
		final String S_ProcName = "CFBamRamSchemaTweakTable.deleteSchemaTweak() ";
		String classCode;
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaTweakBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteSchemaTweak",
				pkey );
		}
		CFBamSchemaTweakBySchemaIdxKey keySchemaIdx = schema.getFactorySchemaTweak().newSchemaIdxKey();
		keySchemaIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamTweakPKey, CFBamSchemaTweakBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaIdx.get( keySchemaIdx );
		subdict.remove( pkey );

		schema.getTableTweak().deleteTweak( Authorization,
			Buff );
	}
	public void deleteSchemaTweakBySchemaIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argSchemaDefId )
	{
		CFBamSchemaTweakBySchemaIdxKey key = schema.getFactorySchemaTweak().newSchemaIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredSchemaDefId( argSchemaDefId );
		deleteSchemaTweakBySchemaIdx( Authorization, key );
	}

	public void deleteSchemaTweakBySchemaIdx( CFSecAuthorization Authorization,
		CFBamSchemaTweakBySchemaIdxKey argKey )
	{
		CFBamSchemaTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaTweakBuff> matchSet = new LinkedList<CFBamSchemaTweakBuff>();
		Iterator<CFBamSchemaTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaTweak( Authorization, cur );
		}
	}

	public void deleteSchemaTweakByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteSchemaTweakByIdIdx( Authorization, key );
	}

	public void deleteSchemaTweakByIdIdx( CFSecAuthorization Authorization,
		CFBamTweakPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamSchemaTweakBuff cur;
		LinkedList<CFBamSchemaTweakBuff> matchSet = new LinkedList<CFBamSchemaTweakBuff>();
		Iterator<CFBamSchemaTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaTweak( Authorization, cur );
		}
	}

	public void deleteSchemaTweakByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId,
		String argName )
	{
		CFBamTweakByUNameIdxKey key = schema.getFactoryTweak().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteSchemaTweakByUNameIdx( Authorization, key );
	}

	public void deleteSchemaTweakByUNameIdx( CFSecAuthorization Authorization,
		CFBamTweakByUNameIdxKey argKey )
	{
		CFBamSchemaTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaTweakBuff> matchSet = new LinkedList<CFBamSchemaTweakBuff>();
		Iterator<CFBamSchemaTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaTweak( Authorization, cur );
		}
	}

	public void deleteSchemaTweakByUDefIdx( CFSecAuthorization Authorization,
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
		deleteSchemaTweakByUDefIdx( Authorization, key );
	}

	public void deleteSchemaTweakByUDefIdx( CFSecAuthorization Authorization,
		CFBamTweakByUDefIdxKey argKey )
	{
		CFBamSchemaTweakBuff cur;
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
		LinkedList<CFBamSchemaTweakBuff> matchSet = new LinkedList<CFBamSchemaTweakBuff>();
		Iterator<CFBamSchemaTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaTweak( Authorization, cur );
		}
	}

	public void deleteSchemaTweakByValTentIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamTweakByValTentIdxKey key = schema.getFactoryTweak().newValTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteSchemaTweakByValTentIdx( Authorization, key );
	}

	public void deleteSchemaTweakByValTentIdx( CFSecAuthorization Authorization,
		CFBamTweakByValTentIdxKey argKey )
	{
		CFBamSchemaTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaTweakBuff> matchSet = new LinkedList<CFBamSchemaTweakBuff>();
		Iterator<CFBamSchemaTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaTweak( Authorization, cur );
		}
	}

	public void deleteSchemaTweakByScopeIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId )
	{
		CFBamTweakByScopeIdxKey key = schema.getFactoryTweak().newScopeIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		deleteSchemaTweakByScopeIdx( Authorization, key );
	}

	public void deleteSchemaTweakByScopeIdx( CFSecAuthorization Authorization,
		CFBamTweakByScopeIdxKey argKey )
	{
		CFBamSchemaTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaTweakBuff> matchSet = new LinkedList<CFBamSchemaTweakBuff>();
		Iterator<CFBamSchemaTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaTweak( Authorization, cur );
		}
	}

	public void deleteSchemaTweakByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamTweakByDefSchemaIdxKey key = schema.getFactoryTweak().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteSchemaTweakByDefSchemaIdx( Authorization, key );
	}

	public void deleteSchemaTweakByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamTweakByDefSchemaIdxKey argKey )
	{
		CFBamSchemaTweakBuff cur;
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
		LinkedList<CFBamSchemaTweakBuff> matchSet = new LinkedList<CFBamSchemaTweakBuff>();
		Iterator<CFBamSchemaTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaTweak( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
