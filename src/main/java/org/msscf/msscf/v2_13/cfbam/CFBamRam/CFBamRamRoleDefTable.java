
// Description: Java 11 in-memory RAM DbIO implementation for RoleDef.

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
 *	CFBamRamRoleDefTable in-memory RAM DbIO implementation
 *	for RoleDef.
 */
public class CFBamRamRoleDefTable
	implements ICFBamRoleDefTable
{
	private ICFBamSchema schema;
	private Map< CFBamRoleDefPKey,
				CFBamRoleDefBuff > dictByPKey
		= new HashMap< CFBamRoleDefPKey,
				CFBamRoleDefBuff >();
	private Map< CFBamRoleDefByUNameIdxKey,
			CFBamRoleDefBuff > dictByUNameIdx
		= new HashMap< CFBamRoleDefByUNameIdxKey,
			CFBamRoleDefBuff >();
	private Map< CFBamRoleDefByUDefIdxKey,
			CFBamRoleDefBuff > dictByUDefIdx
		= new HashMap< CFBamRoleDefByUDefIdxKey,
			CFBamRoleDefBuff >();
	private Map< CFBamRoleDefByRoleDefTentIdxKey,
				Map< CFBamRoleDefPKey,
					CFBamRoleDefBuff >> dictByRoleDefTentIdx
		= new HashMap< CFBamRoleDefByRoleDefTentIdxKey,
				Map< CFBamRoleDefPKey,
					CFBamRoleDefBuff >>();
	private Map< CFBamRoleDefByScopeIdxKey,
				Map< CFBamRoleDefPKey,
					CFBamRoleDefBuff >> dictByScopeIdx
		= new HashMap< CFBamRoleDefByScopeIdxKey,
				Map< CFBamRoleDefPKey,
					CFBamRoleDefBuff >>();
	private Map< CFBamRoleDefByDefSchemaIdxKey,
				Map< CFBamRoleDefPKey,
					CFBamRoleDefBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamRoleDefByDefSchemaIdxKey,
				Map< CFBamRoleDefPKey,
					CFBamRoleDefBuff >>();

	public CFBamRamRoleDefTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createRoleDef( CFSecAuthorization Authorization,
		CFBamRoleDefBuff Buff )
	{
		final String S_ProcName = "createRoleDef";
		CFBamRoleDefPKey pkey = schema.getFactoryRoleDef().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( ((CFBamRamTenantTable)schema.getTableTenant()).nextRoleIdGen( Authorization,
			Buff.getRequiredTenantId() ) );
		Buff.setRequiredTenantId( pkey.getRequiredTenantId() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamRoleDefByUNameIdxKey keyUNameIdx = schema.getFactoryRoleDef().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamRoleDefByUDefIdxKey keyUDefIdx = schema.getFactoryRoleDef().newUDefIdxKey();
		keyUDefIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyUDefIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyUDefIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		keyUDefIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );
		keyUDefIdx.setRequiredName( Buff.getRequiredName() );

		CFBamRoleDefByRoleDefTentIdxKey keyRoleDefTentIdx = schema.getFactoryRoleDef().newRoleDefTentIdxKey();
		keyRoleDefTentIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamRoleDefByScopeIdxKey keyScopeIdx = schema.getFactoryRoleDef().newScopeIdxKey();
		keyScopeIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamRoleDefByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryRoleDef().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"RoleDefUNameIdx",
				keyUNameIdx );
		}

		if( dictByUDefIdx.containsKey( keyUDefIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"RoleDefUDefIdx",
				keyUDefIdx );
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

		dictByUDefIdx.put( keyUDefIdx, Buff );

		Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdictRoleDefTentIdx;
		if( dictByRoleDefTentIdx.containsKey( keyRoleDefTentIdx ) ) {
			subdictRoleDefTentIdx = dictByRoleDefTentIdx.get( keyRoleDefTentIdx );
		}
		else {
			subdictRoleDefTentIdx = new HashMap< CFBamRoleDefPKey, CFBamRoleDefBuff >();
			dictByRoleDefTentIdx.put( keyRoleDefTentIdx, subdictRoleDefTentIdx );
		}
		subdictRoleDefTentIdx.put( pkey, Buff );

		Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdictScopeIdx;
		if( dictByScopeIdx.containsKey( keyScopeIdx ) ) {
			subdictScopeIdx = dictByScopeIdx.get( keyScopeIdx );
		}
		else {
			subdictScopeIdx = new HashMap< CFBamRoleDefPKey, CFBamRoleDefBuff >();
			dictByScopeIdx.put( keyScopeIdx, subdictScopeIdx );
		}
		subdictScopeIdx.put( pkey, Buff );

		Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamRoleDefPKey, CFBamRoleDefBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

	}

	public CFBamRoleDefBuff readDerived( CFSecAuthorization Authorization,
		CFBamRoleDefPKey PKey )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerived";
		CFBamRoleDefPKey key = schema.getFactoryRoleDef().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamRoleDefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRoleDefBuff lockDerived( CFSecAuthorization Authorization,
		CFBamRoleDefPKey PKey )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerived";
		CFBamRoleDefPKey key = schema.getFactoryRoleDef().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamRoleDefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRoleDefBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamRoleDef.readAllDerived";
		CFBamRoleDefBuff[] retList = new CFBamRoleDefBuff[ dictByPKey.values().size() ];
		Iterator< CFBamRoleDefBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamRoleDefBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByUNameIdx";
		CFBamRoleDefByUNameIdxKey key = schema.getFactoryRoleDef().newUNameIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredScopeId( ScopeId );
		key.setRequiredName( Name );

		CFBamRoleDefBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRoleDefBuff readDerivedByUDefIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByUDefIdx";
		CFBamRoleDefByUDefIdxKey key = schema.getFactoryRoleDef().newUDefIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredScopeId( ScopeId );
		key.setOptionalDefSchemaTenantId( DefSchemaTenantId );
		key.setOptionalDefSchemaId( DefSchemaId );
		key.setRequiredName( Name );

		CFBamRoleDefBuff buff;
		if( dictByUDefIdx.containsKey( key ) ) {
			buff = dictByUDefIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRoleDefBuff[] readDerivedByRoleDefTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByRoleDefTentIdx";
		CFBamRoleDefByRoleDefTentIdxKey key = schema.getFactoryRoleDef().newRoleDefTentIdxKey();
		key.setRequiredTenantId( TenantId );

		CFBamRoleDefBuff[] recArray;
		if( dictByRoleDefTentIdx.containsKey( key ) ) {
			Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdictRoleDefTentIdx
				= dictByRoleDefTentIdx.get( key );
			recArray = new CFBamRoleDefBuff[ subdictRoleDefTentIdx.size() ];
			Iterator< CFBamRoleDefBuff > iter = subdictRoleDefTentIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdictRoleDefTentIdx
				= new HashMap< CFBamRoleDefPKey, CFBamRoleDefBuff >();
			dictByRoleDefTentIdx.put( key, subdictRoleDefTentIdx );
			recArray = new CFBamRoleDefBuff[0];
		}
		return( recArray );
	}

	public CFBamRoleDefBuff[] readDerivedByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByScopeIdx";
		CFBamRoleDefByScopeIdxKey key = schema.getFactoryRoleDef().newScopeIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredScopeId( ScopeId );

		CFBamRoleDefBuff[] recArray;
		if( dictByScopeIdx.containsKey( key ) ) {
			Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdictScopeIdx
				= dictByScopeIdx.get( key );
			recArray = new CFBamRoleDefBuff[ subdictScopeIdx.size() ];
			Iterator< CFBamRoleDefBuff > iter = subdictScopeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdictScopeIdx
				= new HashMap< CFBamRoleDefPKey, CFBamRoleDefBuff >();
			dictByScopeIdx.put( key, subdictScopeIdx );
			recArray = new CFBamRoleDefBuff[0];
		}
		return( recArray );
	}

	public CFBamRoleDefBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByDefSchemaIdx";
		CFBamRoleDefByDefSchemaIdxKey key = schema.getFactoryRoleDef().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( DefSchemaTenantId );
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamRoleDefBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamRoleDefBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamRoleDefBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdictDefSchemaIdx
				= new HashMap< CFBamRoleDefPKey, CFBamRoleDefBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamRoleDefBuff[0];
		}
		return( recArray );
	}

	public CFBamRoleDefBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByIdIdx() ";
		CFBamRoleDefPKey key = schema.getFactoryRoleDef().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamRoleDefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRoleDefBuff readBuff( CFSecAuthorization Authorization,
		CFBamRoleDefPKey PKey )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuff";
		CFBamRoleDefBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88d" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamRoleDefBuff lockBuff( CFSecAuthorization Authorization,
		CFBamRoleDefPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamRoleDefBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a88d" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamRoleDefBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamRoleDef.readAllBuff";
		CFBamRoleDefBuff buff;
		ArrayList<CFBamRoleDefBuff> filteredList = new ArrayList<CFBamRoleDefBuff>();
		CFBamRoleDefBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamRoleDefBuff[0] ) );
	}

	public CFBamRoleDefBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByIdIdx() ";
		CFBamRoleDefBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
			return( (CFBamRoleDefBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamRoleDefBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByUNameIdx() ";
		CFBamRoleDefBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			ScopeId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
			return( (CFBamRoleDefBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamRoleDefBuff readBuffByUDefIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByUDefIdx() ";
		CFBamRoleDefBuff buff = readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
			return( (CFBamRoleDefBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamRoleDefBuff[] readBuffByRoleDefTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByRoleDefTentIdx() ";
		CFBamRoleDefBuff buff;
		ArrayList<CFBamRoleDefBuff> filteredList = new ArrayList<CFBamRoleDefBuff>();
		CFBamRoleDefBuff[] buffList = readDerivedByRoleDefTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
				filteredList.add( (CFBamRoleDefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRoleDefBuff[0] ) );
	}

	public CFBamRoleDefBuff[] readBuffByScopeIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ScopeId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByScopeIdx() ";
		CFBamRoleDefBuff buff;
		ArrayList<CFBamRoleDefBuff> filteredList = new ArrayList<CFBamRoleDefBuff>();
		CFBamRoleDefBuff[] buffList = readDerivedByScopeIdx( Authorization,
			TenantId,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
				filteredList.add( (CFBamRoleDefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRoleDefBuff[0] ) );
	}

	public CFBamRoleDefBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readBuffByDefSchemaIdx() ";
		CFBamRoleDefBuff buff;
		ArrayList<CFBamRoleDefBuff> filteredList = new ArrayList<CFBamRoleDefBuff>();
		CFBamRoleDefBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a88d" ) ) {
				filteredList.add( (CFBamRoleDefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRoleDefBuff[0] ) );
	}

	public void updateRoleDef( CFSecAuthorization Authorization,
		CFBamRoleDefBuff Buff )
	{
		CFBamRoleDefPKey pkey = schema.getFactoryRoleDef().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamRoleDefBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateRoleDef",
				"Existing record not found",
				"RoleDef",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateRoleDef",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamRoleDefByUNameIdxKey existingKeyUNameIdx = schema.getFactoryRoleDef().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamRoleDefByUNameIdxKey newKeyUNameIdx = schema.getFactoryRoleDef().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamRoleDefByUDefIdxKey existingKeyUDefIdx = schema.getFactoryRoleDef().newUDefIdxKey();
		existingKeyUDefIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyUDefIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyUDefIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		existingKeyUDefIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );
		existingKeyUDefIdx.setRequiredName( existing.getRequiredName() );

		CFBamRoleDefByUDefIdxKey newKeyUDefIdx = schema.getFactoryRoleDef().newUDefIdxKey();
		newKeyUDefIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyUDefIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyUDefIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		newKeyUDefIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );
		newKeyUDefIdx.setRequiredName( Buff.getRequiredName() );

		CFBamRoleDefByRoleDefTentIdxKey existingKeyRoleDefTentIdx = schema.getFactoryRoleDef().newRoleDefTentIdxKey();
		existingKeyRoleDefTentIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamRoleDefByRoleDefTentIdxKey newKeyRoleDefTentIdx = schema.getFactoryRoleDef().newRoleDefTentIdxKey();
		newKeyRoleDefTentIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamRoleDefByScopeIdxKey existingKeyScopeIdx = schema.getFactoryRoleDef().newScopeIdxKey();
		existingKeyScopeIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamRoleDefByScopeIdxKey newKeyScopeIdx = schema.getFactoryRoleDef().newScopeIdxKey();
		newKeyScopeIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamRoleDefByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryRoleDef().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamRoleDefByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryRoleDef().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateRoleDef",
					"RoleDefUNameIdx",
					newKeyUNameIdx );
			}
		}

		if( ! existingKeyUDefIdx.equals( newKeyUDefIdx ) ) {
			if( dictByUDefIdx.containsKey( newKeyUDefIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateRoleDef",
					"RoleDefUDefIdx",
					newKeyUDefIdx );
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
						"updateRoleDef",
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
						"updateRoleDef",
						"Container",
						"Scope",
						"Scope",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		dictByUDefIdx.remove( existingKeyUDefIdx );
		dictByUDefIdx.put( newKeyUDefIdx, Buff );

		subdict = dictByRoleDefTentIdx.get( existingKeyRoleDefTentIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRoleDefTentIdx.containsKey( newKeyRoleDefTentIdx ) ) {
			subdict = dictByRoleDefTentIdx.get( newKeyRoleDefTentIdx );
		}
		else {
			subdict = new HashMap< CFBamRoleDefPKey, CFBamRoleDefBuff >();
			dictByRoleDefTentIdx.put( newKeyRoleDefTentIdx, subdict );
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
			subdict = new HashMap< CFBamRoleDefPKey, CFBamRoleDefBuff >();
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
			subdict = new HashMap< CFBamRoleDefPKey, CFBamRoleDefBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteRoleDef( CFSecAuthorization Authorization,
		CFBamRoleDefBuff Buff )
	{
		final String S_ProcName = "CFBamRamRoleDefTable.deleteRoleDef() ";
		String classCode;
		CFBamRoleDefPKey pkey = schema.getFactoryRoleDef().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamRoleDefBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteRoleDef",
				pkey );
		}
		CFBamRoleDefByUNameIdxKey keyUNameIdx = schema.getFactoryRoleDef().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamRoleDefByUDefIdxKey keyUDefIdx = schema.getFactoryRoleDef().newUDefIdxKey();
		keyUDefIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyUDefIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyUDefIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		keyUDefIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );
		keyUDefIdx.setRequiredName( existing.getRequiredName() );

		CFBamRoleDefByRoleDefTentIdxKey keyRoleDefTentIdx = schema.getFactoryRoleDef().newRoleDefTentIdxKey();
		keyRoleDefTentIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamRoleDefByScopeIdxKey keyScopeIdx = schema.getFactoryRoleDef().newScopeIdxKey();
		keyScopeIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamRoleDefByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryRoleDef().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
					existing.getRequiredTenantId(),
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRoleDef",
				"Superclass",
				"SuperClass",
				"SchemaRole",
				pkey );
		}

		// Delete is valid
		Map< CFBamRoleDefPKey, CFBamRoleDefBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		dictByUDefIdx.remove( keyUDefIdx );

		subdict = dictByRoleDefTentIdx.get( keyRoleDefTentIdx );
		subdict.remove( pkey );

		subdict = dictByScopeIdx.get( keyScopeIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

	}
	public void deleteRoleDefByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamRoleDefPKey key = schema.getFactoryRoleDef().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteRoleDefByIdIdx( Authorization, key );
	}

	public void deleteRoleDefByIdIdx( CFSecAuthorization Authorization,
		CFBamRoleDefPKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamRoleDefBuff cur;
		LinkedList<CFBamRoleDefBuff> matchSet = new LinkedList<CFBamRoleDefBuff>();
		Iterator<CFBamRoleDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRoleDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88d".equals( subClassCode ) ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( "a88e".equals( subClassCode ) ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (CFBamSchemaRoleBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of RoleDef must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteRoleDefByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId,
		String argName )
	{
		CFBamRoleDefByUNameIdxKey key = schema.getFactoryRoleDef().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteRoleDefByUNameIdx( Authorization, key );
	}

	public void deleteRoleDefByUNameIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByUNameIdx";
		CFBamRoleDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRoleDefBuff> matchSet = new LinkedList<CFBamRoleDefBuff>();
		Iterator<CFBamRoleDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRoleDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88d".equals( subClassCode ) ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( "a88e".equals( subClassCode ) ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (CFBamSchemaRoleBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of RoleDef must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteRoleDefByUDefIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId,
		Long argDefSchemaTenantId,
		Long argDefSchemaId,
		String argName )
	{
		CFBamRoleDefByUDefIdxKey key = schema.getFactoryRoleDef().newUDefIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		key.setRequiredName( argName );
		deleteRoleDefByUDefIdx( Authorization, key );
	}

	public void deleteRoleDefByUDefIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByUDefIdxKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByUDefIdx";
		CFBamRoleDefBuff cur;
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
		LinkedList<CFBamRoleDefBuff> matchSet = new LinkedList<CFBamRoleDefBuff>();
		Iterator<CFBamRoleDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRoleDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88d".equals( subClassCode ) ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( "a88e".equals( subClassCode ) ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (CFBamSchemaRoleBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of RoleDef must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteRoleDefByRoleDefTentIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamRoleDefByRoleDefTentIdxKey key = schema.getFactoryRoleDef().newRoleDefTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteRoleDefByRoleDefTentIdx( Authorization, key );
	}

	public void deleteRoleDefByRoleDefTentIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByRoleDefTentIdxKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByRoleDefTentIdx";
		CFBamRoleDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRoleDefBuff> matchSet = new LinkedList<CFBamRoleDefBuff>();
		Iterator<CFBamRoleDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRoleDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88d".equals( subClassCode ) ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( "a88e".equals( subClassCode ) ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (CFBamSchemaRoleBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of RoleDef must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteRoleDefByScopeIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argScopeId )
	{
		CFBamRoleDefByScopeIdxKey key = schema.getFactoryRoleDef().newScopeIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		deleteRoleDefByScopeIdx( Authorization, key );
	}

	public void deleteRoleDefByScopeIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByScopeIdxKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByScopeIdx";
		CFBamRoleDefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRoleDefBuff> matchSet = new LinkedList<CFBamRoleDefBuff>();
		Iterator<CFBamRoleDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRoleDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88d".equals( subClassCode ) ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( "a88e".equals( subClassCode ) ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (CFBamSchemaRoleBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of RoleDef must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteRoleDefByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamRoleDefByDefSchemaIdxKey key = schema.getFactoryRoleDef().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteRoleDefByDefSchemaIdx( Authorization, key );
	}

	public void deleteRoleDefByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamRoleDefByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByDefSchemaIdx";
		CFBamRoleDefBuff cur;
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
		LinkedList<CFBamRoleDefBuff> matchSet = new LinkedList<CFBamRoleDefBuff>();
		Iterator<CFBamRoleDefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRoleDefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a88d".equals( subClassCode ) ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( "a88e".equals( subClassCode ) ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (CFBamSchemaRoleBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of RoleDef must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void releasePreparedStatements() {
	}
}
