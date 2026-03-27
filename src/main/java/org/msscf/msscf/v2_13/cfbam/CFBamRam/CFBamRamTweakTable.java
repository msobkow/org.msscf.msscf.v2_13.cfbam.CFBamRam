
// Description: Java 11 in-memory RAM DbIO implementation for Tweak.

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
 *	CFBamRamTweakTable in-memory RAM DbIO implementation
 *	for Tweak.
 */
public class CFBamRamTweakTable
	implements ICFBamTweakTable
{
	private ICFBamSchema schema;
	private Map< CFBamTweakPKey,
				CFBamTweakBuff > dictByPKey
		= new HashMap< CFBamTweakPKey,
				CFBamTweakBuff >();
	private Map< CFBamTweakByUNameIdxKey,
			CFBamTweakBuff > dictByUNameIdx
		= new HashMap< CFBamTweakByUNameIdxKey,
			CFBamTweakBuff >();
	private Map< CFBamTweakByValTentIdxKey,
				Map< CFBamTweakPKey,
					CFBamTweakBuff >> dictByValTentIdx
		= new HashMap< CFBamTweakByValTentIdxKey,
				Map< CFBamTweakPKey,
					CFBamTweakBuff >>();
	private Map< CFBamTweakByScopeIdxKey,
				Map< CFBamTweakPKey,
					CFBamTweakBuff >> dictByScopeIdx
		= new HashMap< CFBamTweakByScopeIdxKey,
				Map< CFBamTweakPKey,
					CFBamTweakBuff >>();
	private Map< CFBamTweakByDefSchemaIdxKey,
				Map< CFBamTweakPKey,
					CFBamTweakBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamTweakByDefSchemaIdxKey,
				Map< CFBamTweakPKey,
					CFBamTweakBuff >>();

	public CFBamRamTweakTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createTweak( CFSecAuthorization Authorization,
		CFBamTweakBuff Buff )
	{
		final String S_ProcName = "createTweak";
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( ((CFBamRamTenantTable)schema.getTableTenant()).nextTweakIdGen( Authorization,
			Buff.getRequiredTenantId() ) );
		Buff.setRequiredTenantId( pkey.getRequiredTenantId() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamTweakByUNameIdxKey keyUNameIdx = schema.getFactoryTweak().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamTweakByValTentIdxKey keyValTentIdx = schema.getFactoryTweak().newValTentIdxKey();
		keyValTentIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamTweakByScopeIdxKey keyScopeIdx = schema.getFactoryTweak().newScopeIdxKey();
		keyScopeIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamTweakByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryTweak().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"TweakUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Owner",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredScopeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Scope",
						"Scope",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamTweakPKey, CFBamTweakBuff > subdictValTentIdx;
		if( dictByValTentIdx.containsKey( keyValTentIdx ) ) {
			subdictValTentIdx = dictByValTentIdx.get( keyValTentIdx );
		}
		else {
			subdictValTentIdx = new HashMap< CFBamTweakPKey, CFBamTweakBuff >();
			dictByValTentIdx.put( keyValTentIdx, subdictValTentIdx );
		}
		subdictValTentIdx.put( pkey, Buff );

		Map< CFBamTweakPKey, CFBamTweakBuff > subdictScopeIdx;
		if( dictByScopeIdx.containsKey( keyScopeIdx ) ) {
			subdictScopeIdx = dictByScopeIdx.get( keyScopeIdx );
		}
		else {
			subdictScopeIdx = new HashMap< CFBamTweakPKey, CFBamTweakBuff >();
			dictByScopeIdx.put( keyScopeIdx, subdictScopeIdx );
		}
		subdictScopeIdx.put( pkey, Buff );

		Map< CFBamTweakPKey, CFBamTweakBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamTweakPKey, CFBamTweakBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

	}

	public CFBamTweakBuff readDerived( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamTweak.readDerived";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTweakBuff lockDerived( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamTweak.readDerived";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTweakBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamTweak.readAllDerived";
		CFBamTweakBuff[] retList = new CFBamTweakBuff[ dictByPKey.values().size() ];
		Iterator< CFBamTweakBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamTweakBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByUNameIdx";
		CFBamTweakByUNameIdxKey key = schema.getFactoryTweak().newUNameIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredScopeId( ScopeId );
		key.setRequiredName( Name );

		CFBamTweakBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTweakBuff[] readDerivedByValTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByValTentIdx";
		CFBamTweakByValTentIdxKey key = schema.getFactoryTweak().newValTentIdxKey();
		key.setRequiredTenantId( TenantId );

		CFBamTweakBuff[] recArray;
		if( dictByValTentIdx.containsKey( key ) ) {
			Map< CFBamTweakPKey, CFBamTweakBuff > subdictValTentIdx
				= dictByValTentIdx.get( key );
			recArray = new CFBamTweakBuff[ subdictValTentIdx.size() ];
			Iterator< CFBamTweakBuff > iter = subdictValTentIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamTweakPKey, CFBamTweakBuff > subdictValTentIdx
				= new HashMap< CFBamTweakPKey, CFBamTweakBuff >();
			dictByValTentIdx.put( key, subdictValTentIdx );
			recArray = new CFBamTweakBuff[0];
		}
		return( recArray );
	}

	public CFBamTweakBuff[] readDerivedByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByScopeIdx";
		CFBamTweakByScopeIdxKey key = schema.getFactoryTweak().newScopeIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredScopeId( ScopeId );

		CFBamTweakBuff[] recArray;
		if( dictByScopeIdx.containsKey( key ) ) {
			Map< CFBamTweakPKey, CFBamTweakBuff > subdictScopeIdx
				= dictByScopeIdx.get( key );
			recArray = new CFBamTweakBuff[ subdictScopeIdx.size() ];
			Iterator< CFBamTweakBuff > iter = subdictScopeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamTweakPKey, CFBamTweakBuff > subdictScopeIdx
				= new HashMap< CFBamTweakPKey, CFBamTweakBuff >();
			dictByScopeIdx.put( key, subdictScopeIdx );
			recArray = new CFBamTweakBuff[0];
		}
		return( recArray );
	}

	public CFBamTweakBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByDefSchemaIdx";
		CFBamTweakByDefSchemaIdxKey key = schema.getFactoryTweak().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( DefSchemaTenantId );
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamTweakBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamTweakPKey, CFBamTweakBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamTweakBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamTweakBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamTweakPKey, CFBamTweakBuff > subdictDefSchemaIdx
				= new HashMap< CFBamTweakPKey, CFBamTweakBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamTweakBuff[0];
		}
		return( recArray );
	}

	public CFBamTweakBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByIdIdx() ";
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamTweakBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTweakBuff readBuff( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "CFBamRamTweak.readBuff";
		CFBamTweakBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88a" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamTweakBuff lockBuff( CFSecAuthorization Authorization,
		CFBamTweakPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamTweakBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88a" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamTweakBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamTweak.readAllBuff";
		CFBamTweakBuff buff;
		ArrayList<CFBamTweakBuff> filteredList = new ArrayList<CFBamTweakBuff>();
		CFBamTweakBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamTweakBuff[0] ) );
	}

	public CFBamTweakBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByIdIdx() ";
		CFBamTweakBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
			return( (CFBamTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamTweakBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByUNameIdx() ";
		CFBamTweakBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			ScopeId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
			return( (CFBamTweakBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamTweakBuff[] readBuffByValTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByValTentIdx() ";
		CFBamTweakBuff buff;
		ArrayList<CFBamTweakBuff> filteredList = new ArrayList<CFBamTweakBuff>();
		CFBamTweakBuff[] buffList = readDerivedByValTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( (CFBamTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTweakBuff[0] ) );
	}

	public CFBamTweakBuff[] readBuffByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByScopeIdx() ";
		CFBamTweakBuff buff;
		ArrayList<CFBamTweakBuff> filteredList = new ArrayList<CFBamTweakBuff>();
		CFBamTweakBuff[] buffList = readDerivedByScopeIdx( Authorization,
			TenantId,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( (CFBamTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTweakBuff[0] ) );
	}

	public CFBamTweakBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readBuffByDefSchemaIdx() ";
		CFBamTweakBuff buff;
		ArrayList<CFBamTweakBuff> filteredList = new ArrayList<CFBamTweakBuff>();
		CFBamTweakBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88a" ) ) {
				filteredList.add( (CFBamTweakBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTweakBuff[0] ) );
	}

	public void updateTweak( CFSecAuthorization Authorization,
		CFBamTweakBuff Buff )
	{
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamTweakBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateTweak",
				"Existing record not found",
				"Tweak",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateTweak",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamTweakByUNameIdxKey existingKeyUNameIdx = schema.getFactoryTweak().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamTweakByUNameIdxKey newKeyUNameIdx = schema.getFactoryTweak().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamTweakByValTentIdxKey existingKeyValTentIdx = schema.getFactoryTweak().newValTentIdxKey();
		existingKeyValTentIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamTweakByValTentIdxKey newKeyValTentIdx = schema.getFactoryTweak().newValTentIdxKey();
		newKeyValTentIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamTweakByScopeIdxKey existingKeyScopeIdx = schema.getFactoryTweak().newScopeIdxKey();
		existingKeyScopeIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamTweakByScopeIdxKey newKeyScopeIdx = schema.getFactoryTweak().newScopeIdxKey();
		newKeyScopeIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamTweakByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryTweak().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamTweakByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryTweak().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateTweak",
					"TweakUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTweak",
						"Owner",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredScopeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTweak",
						"Container",
						"Scope",
						"Scope",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamTweakPKey, CFBamTweakBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByValTentIdx.get( existingKeyValTentIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByValTentIdx.containsKey( newKeyValTentIdx ) ) {
			subdict = dictByValTentIdx.get( newKeyValTentIdx );
		}
		else {
			subdict = new HashMap< CFBamTweakPKey, CFBamTweakBuff >();
			dictByValTentIdx.put( newKeyValTentIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByScopeIdx.get( existingKeyScopeIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByScopeIdx.containsKey( newKeyScopeIdx ) ) {
			subdict = dictByScopeIdx.get( newKeyScopeIdx );
		}
		else {
			subdict = new HashMap< CFBamTweakPKey, CFBamTweakBuff >();
			dictByScopeIdx.put( newKeyScopeIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamTweakPKey, CFBamTweakBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteTweak( CFSecAuthorization Authorization,
		CFBamTweakBuff Buff )
	{
		final String S_ProcName = "CFBamRamTweakTable.deleteTweak() ";
		String classCode;
		CFBamTweakPKey pkey = schema.getFactoryTweak().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamTweakBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteTweak",
				pkey );
		}
		CFBamTweakByUNameIdxKey keyUNameIdx = schema.getFactoryTweak().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamTweakByValTentIdxKey keyValTentIdx = schema.getFactoryTweak().newValTentIdxKey();
		keyValTentIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamTweakByScopeIdxKey keyScopeIdx = schema.getFactoryTweak().newScopeIdxKey();
		keyScopeIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamTweakByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryTweak().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
					existing.getRequiredTenantId(),
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteTweak",
				"Superclass",
				"SuperClass",
				"TableTweak",
				pkey );
		}

		if( schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
					existing.getRequiredTenantId(),
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteTweak",
				"Superclass",
				"SuperClass",
				"SchemaTweak",
				pkey );
		}

		// Delete is valid
		Map< CFBamTweakPKey, CFBamTweakBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByValTentIdx.get( keyValTentIdx );
		subdict.remove( pkey );

		subdict = dictByScopeIdx.get( keyScopeIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

	}
	public void deleteTweakByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamTweakPKey key = schema.getFactoryTweak().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteTweakByIdIdx( Authorization, key );
	}

	public void deleteTweakByIdIdx( CFSecAuthorization Authorization,
		CFBamTweakPKey argKey )
	{
		final String S_ProcName = "deleteTweakByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamTweakBuff cur;
		LinkedList<CFBamTweakBuff> matchSet = new LinkedList<CFBamTweakBuff>();
		Iterator<CFBamTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88a".equals( subClassCode ) ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( "a88b".equals( subClassCode ) ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (CFBamTableTweakBuff)cur );
			}
			else if( "a88c".equals( subClassCode ) ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (CFBamSchemaTweakBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Tweak must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteTweakByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId,
		String argName )
	{
		CFBamTweakByUNameIdxKey key = schema.getFactoryTweak().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteTweakByUNameIdx( Authorization, key );
	}

	public void deleteTweakByUNameIdx( CFSecAuthorization Authorization,
		CFBamTweakByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteTweakByUNameIdx";
		CFBamTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTweakBuff> matchSet = new LinkedList<CFBamTweakBuff>();
		Iterator<CFBamTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88a".equals( subClassCode ) ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( "a88b".equals( subClassCode ) ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (CFBamTableTweakBuff)cur );
			}
			else if( "a88c".equals( subClassCode ) ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (CFBamSchemaTweakBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Tweak must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteTweakByValTentIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamTweakByValTentIdxKey key = schema.getFactoryTweak().newValTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteTweakByValTentIdx( Authorization, key );
	}

	public void deleteTweakByValTentIdx( CFSecAuthorization Authorization,
		CFBamTweakByValTentIdxKey argKey )
	{
		final String S_ProcName = "deleteTweakByValTentIdx";
		CFBamTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTweakBuff> matchSet = new LinkedList<CFBamTweakBuff>();
		Iterator<CFBamTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88a".equals( subClassCode ) ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( "a88b".equals( subClassCode ) ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (CFBamTableTweakBuff)cur );
			}
			else if( "a88c".equals( subClassCode ) ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (CFBamSchemaTweakBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Tweak must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteTweakByScopeIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId )
	{
		CFBamTweakByScopeIdxKey key = schema.getFactoryTweak().newScopeIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		deleteTweakByScopeIdx( Authorization, key );
	}

	public void deleteTweakByScopeIdx( CFSecAuthorization Authorization,
		CFBamTweakByScopeIdxKey argKey )
	{
		final String S_ProcName = "deleteTweakByScopeIdx";
		CFBamTweakBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTweakBuff> matchSet = new LinkedList<CFBamTweakBuff>();
		Iterator<CFBamTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88a".equals( subClassCode ) ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( "a88b".equals( subClassCode ) ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (CFBamTableTweakBuff)cur );
			}
			else if( "a88c".equals( subClassCode ) ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (CFBamSchemaTweakBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Tweak must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteTweakByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamTweakByDefSchemaIdxKey key = schema.getFactoryTweak().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteTweakByDefSchemaIdx( Authorization, key );
	}

	public void deleteTweakByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamTweakByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteTweakByDefSchemaIdx";
		CFBamTweakBuff cur;
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
		LinkedList<CFBamTweakBuff> matchSet = new LinkedList<CFBamTweakBuff>();
		Iterator<CFBamTweakBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTweakBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88a".equals( subClassCode ) ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( "a88b".equals( subClassCode ) ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (CFBamTableTweakBuff)cur );
			}
			else if( "a88c".equals( subClassCode ) ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (CFBamSchemaTweakBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Tweak must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void releasePreparedStatements() {
	}
}
