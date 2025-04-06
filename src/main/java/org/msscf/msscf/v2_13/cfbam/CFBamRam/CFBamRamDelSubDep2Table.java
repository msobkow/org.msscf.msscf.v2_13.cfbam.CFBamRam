
// Description: Java 11 in-memory RAM DbIO implementation for DelSubDep2.

/*
 *	org.msscf.msscf.CFBam
 *
 *	Copyright (c) 2020 Mark Stephen Sobkow
 *	
 *	MSS Code Factory CFBam 2.13 Business Application Model
 *	
 *	Copyright 2020-2021 Mark Stephen Sobkow
 *	
 *		This file is part of MSS Code Factory.
 *	
 *		MSS Code Factory is available under dual commercial license from Mark Stephen
 *		Sobkow, or under the terms of the GNU General Public License, Version 3
 *		or later.
 *	
 *	    MSS Code Factory is free software: you can redistribute it and/or modify
 *	    it under the terms of the GNU General Public License as published by
 *	    the Free Software Foundation, either version 3 of the License, or
 *	    (at your option) any later version.
 *	
 *	    MSS Code Factory is distributed in the hope that it will be useful,
 *	    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	    GNU General Public License for more details.
 *	
 *	    You should have received a copy of the GNU General Public License
 *	    along with MSS Code Factory.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	Donations to support MSS Code Factory can be made at
 *	https://www.paypal.com/paypalme2/MarkSobkow
 *	
 *	Please contact Mark Stephen Sobkow at mark.sobkow@gmail.com for commercial licensing.
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
import org.msscf.msscf.v2_13.cfsec.CFSec.*;
import org.msscf.msscf.v2_13.cfint.CFInt.*;
import org.msscf.msscf.v2_13.cfbam.CFBam.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;
import org.msscf.msscf.v2_13.cfsec.CFSecObj.*;
import org.msscf.msscf.v2_13.cfint.CFIntObj.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamRamDelSubDep2Table in-memory RAM DbIO implementation
 *	for DelSubDep2.
 */
public class CFBamRamDelSubDep2Table
	implements ICFBamDelSubDep2Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamDelSubDep2Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamDelSubDep2Buff >();
	private Map< CFBamDelSubDep2ByContDelDep1IdxKey,
				Map< CFBamScopePKey,
					CFBamDelSubDep2Buff >> dictByContDelDep1Idx
		= new HashMap< CFBamDelSubDep2ByContDelDep1IdxKey,
				Map< CFBamScopePKey,
					CFBamDelSubDep2Buff >>();
	private Map< CFBamDelSubDep2ByUNameIdxKey,
			CFBamDelSubDep2Buff > dictByUNameIdx
		= new HashMap< CFBamDelSubDep2ByUNameIdxKey,
			CFBamDelSubDep2Buff >();

	public CFBamRamDelSubDep2Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createDelSubDep2( CFSecAuthorization Authorization,
		CFBamDelSubDep2Buff Buff )
	{
		final String S_ProcName = "createDelSubDep2";
		schema.getTableDelDep().createDelDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelSubDep2ByContDelDep1IdxKey keyContDelDep1Idx = schema.getFactoryDelSubDep2().newContDelDep1IdxKey();
		keyContDelDep1Idx.setRequiredDelSubDep1TenantId( Buff.getRequiredDelSubDep1TenantId() );
		keyContDelDep1Idx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );

		CFBamDelSubDep2ByUNameIdxKey keyUNameIdx = schema.getFactoryDelSubDep2().newUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep1TenantId( Buff.getRequiredDelSubDep1TenantId() );
		keyUNameIdx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelSubDep2UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"DelDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep1TenantId(),
						Buff.getRequiredDelSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"DelSubDep1",
						"DelSubDep1",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamDelSubDep2Buff > subdictContDelDep1Idx;
		if( dictByContDelDep1Idx.containsKey( keyContDelDep1Idx ) ) {
			subdictContDelDep1Idx = dictByContDelDep1Idx.get( keyContDelDep1Idx );
		}
		else {
			subdictContDelDep1Idx = new HashMap< CFBamScopePKey, CFBamDelSubDep2Buff >();
			dictByContDelDep1Idx.put( keyContDelDep1Idx, subdictContDelDep1Idx );
		}
		subdictContDelDep1Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamDelSubDep2Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelSubDep2Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep2Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelSubDep2Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep2Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelSubDep2.readAllDerived";
		CFBamDelSubDep2Buff[] retList = new CFBamDelSubDep2Buff[ dictByPKey.values().size() ];
		Iterator< CFBamDelSubDep2Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamDelSubDep2Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		CFBamScopeBuff buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamScopeBuff buff;
			ArrayList<CFBamDelSubDep2Buff> filteredList = new ArrayList<CFBamDelSubDep2Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelSubDep2Buff ) ) {
					filteredList.add( (CFBamDelSubDep2Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelSubDep2Buff[0] ) );
		}
	}

	public CFBamDelSubDep2Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		CFBamDelDepBuff buffList[] = schema.getTableDelDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamDelDepBuff buff;
			ArrayList<CFBamDelSubDep2Buff> filteredList = new ArrayList<CFBamDelSubDep2Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelSubDep2Buff ) ) {
					filteredList.add( (CFBamDelSubDep2Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelSubDep2Buff[0] ) );
		}
	}

	public CFBamDelSubDep2Buff[] readDerivedByDelDepIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		CFBamDelDepBuff buffList[] = schema.getTableDelDep().readDerivedByDelDepIdx( Authorization,
			RelationTenantId,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamDelDepBuff buff;
			ArrayList<CFBamDelSubDep2Buff> filteredList = new ArrayList<CFBamDelSubDep2Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelSubDep2Buff ) ) {
					filteredList.add( (CFBamDelSubDep2Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelSubDep2Buff[0] ) );
		}
	}

	public CFBamDelSubDep2Buff[] readDerivedByContDelDep1Idx( CFSecAuthorization Authorization,
		long DelSubDep1TenantId,
		long DelSubDep1Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerivedByContDelDep1Idx";
		CFBamDelSubDep2ByContDelDep1IdxKey key = schema.getFactoryDelSubDep2().newContDelDep1IdxKey();
		key.setRequiredDelSubDep1TenantId( DelSubDep1TenantId );
		key.setRequiredDelSubDep1Id( DelSubDep1Id );

		CFBamDelSubDep2Buff[] recArray;
		if( dictByContDelDep1Idx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamDelSubDep2Buff > subdictContDelDep1Idx
				= dictByContDelDep1Idx.get( key );
			recArray = new CFBamDelSubDep2Buff[ subdictContDelDep1Idx.size() ];
			Iterator< CFBamDelSubDep2Buff > iter = subdictContDelDep1Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamDelSubDep2Buff > subdictContDelDep1Idx
				= new HashMap< CFBamScopePKey, CFBamDelSubDep2Buff >();
			dictByContDelDep1Idx.put( key, subdictContDelDep1Idx );
			recArray = new CFBamDelSubDep2Buff[0];
		}
		return( recArray );
	}

	public CFBamDelSubDep2Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long DelSubDep1TenantId,
		long DelSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerivedByUNameIdx";
		CFBamDelSubDep2ByUNameIdxKey key = schema.getFactoryDelSubDep2().newUNameIdxKey();
		key.setRequiredDelSubDep1TenantId( DelSubDep1TenantId );
		key.setRequiredDelSubDep1Id( DelSubDep1Id );
		key.setRequiredName( Name );

		CFBamDelSubDep2Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep2Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamDelSubDep2Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep2Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readBuff";
		CFBamDelSubDep2Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a819" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep2Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamDelSubDep2Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a819" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep2Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readAllBuff";
		CFBamDelSubDep2Buff buff;
		ArrayList<CFBamDelSubDep2Buff> filteredList = new ArrayList<CFBamDelSubDep2Buff>();
		CFBamDelSubDep2Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a819" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep2Buff[0] ) );
	}

	public CFBamDelSubDep2Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamDelSubDep2Buff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamDelSubDep2Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamDelSubDep2Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamDelSubDep2Buff buff;
		ArrayList<CFBamDelSubDep2Buff> filteredList = new ArrayList<CFBamDelSubDep2Buff>();
		CFBamDelSubDep2Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamDelSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep2Buff[0] ) );
	}

	public CFBamDelSubDep2Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDefSchemaIdx() ";
		CFBamDelSubDep2Buff buff;
		ArrayList<CFBamDelSubDep2Buff> filteredList = new ArrayList<CFBamDelSubDep2Buff>();
		CFBamDelSubDep2Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep2Buff[0] ) );
	}

	public CFBamDelSubDep2Buff[] readBuffByDelDepIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDelDepIdx() ";
		CFBamDelSubDep2Buff buff;
		ArrayList<CFBamDelSubDep2Buff> filteredList = new ArrayList<CFBamDelSubDep2Buff>();
		CFBamDelSubDep2Buff[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationTenantId,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep2Buff[0] ) );
	}

	public CFBamDelSubDep2Buff[] readBuffByContDelDep1Idx( CFSecAuthorization Authorization,
		long DelSubDep1TenantId,
		long DelSubDep1Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readBuffByContDelDep1Idx() ";
		CFBamDelSubDep2Buff buff;
		ArrayList<CFBamDelSubDep2Buff> filteredList = new ArrayList<CFBamDelSubDep2Buff>();
		CFBamDelSubDep2Buff[] buffList = readDerivedByContDelDep1Idx( Authorization,
			DelSubDep1TenantId,
			DelSubDep1Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a819" ) ) {
				filteredList.add( (CFBamDelSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep2Buff[0] ) );
	}

	public CFBamDelSubDep2Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long DelSubDep1TenantId,
		long DelSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readBuffByUNameIdx() ";
		CFBamDelSubDep2Buff buff = readDerivedByUNameIdx( Authorization,
			DelSubDep1TenantId,
			DelSubDep1Id,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a819" ) ) {
			return( (CFBamDelSubDep2Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific DelSubDep2 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDefSchemaTenantId	The DelSubDep2 key attribute of the instance generating the id.
	 *
	 *	@param	argDefSchemaId	The DelSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelSubDep2Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelSubDep2 buffer instances identified by the duplicate key DelDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argRelationTenantId	The DelSubDep2 key attribute of the instance generating the id.
	 *
	 *	@param	argRelationId	The DelSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelSubDep2Buff[] pageBuffByDelDepIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDelDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelSubDep2 buffer instances identified by the duplicate key ContDelDep1Idx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDelSubDep1TenantId	The DelSubDep2 key attribute of the instance generating the id.
	 *
	 *	@param	argDelSubDep1Id	The DelSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelSubDep2Buff[] pageBuffByContDelDep1Idx( CFSecAuthorization Authorization,
		long DelSubDep1TenantId,
		long DelSubDep1Id,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByContDelDep1Idx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateDelSubDep2( CFSecAuthorization Authorization,
		CFBamDelSubDep2Buff Buff )
	{
		schema.getTableDelDep().updateDelDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelSubDep2Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelSubDep2",
				"Existing record not found",
				"DelSubDep2",
				pkey );
		}
		CFBamDelSubDep2ByContDelDep1IdxKey existingKeyContDelDep1Idx = schema.getFactoryDelSubDep2().newContDelDep1IdxKey();
		existingKeyContDelDep1Idx.setRequiredDelSubDep1TenantId( existing.getRequiredDelSubDep1TenantId() );
		existingKeyContDelDep1Idx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );

		CFBamDelSubDep2ByContDelDep1IdxKey newKeyContDelDep1Idx = schema.getFactoryDelSubDep2().newContDelDep1IdxKey();
		newKeyContDelDep1Idx.setRequiredDelSubDep1TenantId( Buff.getRequiredDelSubDep1TenantId() );
		newKeyContDelDep1Idx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );

		CFBamDelSubDep2ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryDelSubDep2().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredDelSubDep1TenantId( existing.getRequiredDelSubDep1TenantId() );
		existingKeyUNameIdx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamDelSubDep2ByUNameIdxKey newKeyUNameIdx = schema.getFactoryDelSubDep2().newUNameIdxKey();
		newKeyUNameIdx.setRequiredDelSubDep1TenantId( Buff.getRequiredDelSubDep1TenantId() );
		newKeyUNameIdx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelSubDep2",
					"DelSubDep2UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep2",
						"Superclass",
						"SuperClass",
						"DelDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep1TenantId(),
						Buff.getRequiredDelSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep2",
						"Container",
						"DelSubDep1",
						"DelSubDep1",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamDelSubDep2Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByContDelDep1Idx.get( existingKeyContDelDep1Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContDelDep1Idx.containsKey( newKeyContDelDep1Idx ) ) {
			subdict = dictByContDelDep1Idx.get( newKeyContDelDep1Idx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamDelSubDep2Buff >();
			dictByContDelDep1Idx.put( newKeyContDelDep1Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deleteDelSubDep2( CFSecAuthorization Authorization,
		CFBamDelSubDep2Buff Buff )
	{
		final String S_ProcName = "CFBamRamDelSubDep2Table.deleteDelSubDep2() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelSubDep2Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelSubDep2",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckDelDep[] = schema.getTableDelSubDep3().readDerivedByDelSubDep2Idx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		if( arrCheckDelDep.length > 0 ) {
			schema.getTableDelSubDep3().deleteDelSubDep3ByDelSubDep2Idx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		}
		CFBamDelSubDep2ByContDelDep1IdxKey keyContDelDep1Idx = schema.getFactoryDelSubDep2().newContDelDep1IdxKey();
		keyContDelDep1Idx.setRequiredDelSubDep1TenantId( existing.getRequiredDelSubDep1TenantId() );
		keyContDelDep1Idx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );

		CFBamDelSubDep2ByUNameIdxKey keyUNameIdx = schema.getFactoryDelSubDep2().newUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep1TenantId( existing.getRequiredDelSubDep1TenantId() );
		keyUNameIdx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamDelSubDep2Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByContDelDep1Idx.get( keyContDelDep1Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableDelDep().deleteDelDep( Authorization,
			Buff );
	}
	public void deleteDelSubDep2ByContDelDep1Idx( CFSecAuthorization Authorization,
		long argDelSubDep1TenantId,
		long argDelSubDep1Id )
	{
		CFBamDelSubDep2ByContDelDep1IdxKey key = schema.getFactoryDelSubDep2().newContDelDep1IdxKey();
		key.setRequiredDelSubDep1TenantId( argDelSubDep1TenantId );
		key.setRequiredDelSubDep1Id( argDelSubDep1Id );
		deleteDelSubDep2ByContDelDep1Idx( Authorization, key );
	}

	public void deleteDelSubDep2ByContDelDep1Idx( CFSecAuthorization Authorization,
		CFBamDelSubDep2ByContDelDep1IdxKey argKey )
	{
		CFBamDelSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep2Buff> matchSet = new LinkedList<CFBamDelSubDep2Buff>();
		Iterator<CFBamDelSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByUNameIdx( CFSecAuthorization Authorization,
		long argDelSubDep1TenantId,
		long argDelSubDep1Id,
		String argName )
	{
		CFBamDelSubDep2ByUNameIdxKey key = schema.getFactoryDelSubDep2().newUNameIdxKey();
		key.setRequiredDelSubDep1TenantId( argDelSubDep1TenantId );
		key.setRequiredDelSubDep1Id( argDelSubDep1Id );
		key.setRequiredName( argName );
		deleteDelSubDep2ByUNameIdx( Authorization, key );
	}

	public void deleteDelSubDep2ByUNameIdx( CFSecAuthorization Authorization,
		CFBamDelSubDep2ByUNameIdxKey argKey )
	{
		CFBamDelSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep2Buff> matchSet = new LinkedList<CFBamDelSubDep2Buff>();
		Iterator<CFBamDelSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamDelDepByDefSchemaIdxKey key = schema.getFactoryDelDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelSubDep2ByDefSchemaIdx( Authorization, key );
	}

	public void deleteDelSubDep2ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamDelSubDep2Buff cur;
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
		LinkedList<CFBamDelSubDep2Buff> matchSet = new LinkedList<CFBamDelSubDep2Buff>();
		Iterator<CFBamDelSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByDelDepIdx( CFSecAuthorization Authorization,
		long argRelationTenantId,
		long argRelationId )
	{
		CFBamDelDepByDelDepIdxKey key = schema.getFactoryDelDep().newDelDepIdxKey();
		key.setRequiredRelationTenantId( argRelationTenantId );
		key.setRequiredRelationId( argRelationId );
		deleteDelSubDep2ByDelDepIdx( Authorization, key );
	}

	public void deleteDelSubDep2ByDelDepIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamDelSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep2Buff> matchSet = new LinkedList<CFBamDelSubDep2Buff>();
		Iterator<CFBamDelSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteDelSubDep2ByIdIdx( Authorization, key );
	}

	public void deleteDelSubDep2ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamDelSubDep2Buff cur;
		LinkedList<CFBamDelSubDep2Buff> matchSet = new LinkedList<CFBamDelSubDep2Buff>();
		Iterator<CFBamDelSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void deleteDelSubDep2ByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelSubDep2ByTenantIdx( Authorization, key );
	}

	public void deleteDelSubDep2ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamDelSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep2Buff> matchSet = new LinkedList<CFBamDelSubDep2Buff>();
		Iterator<CFBamDelSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep2( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
