
// Description: Java 11 in-memory RAM DbIO implementation for DelSubDep3.

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
 *	CFBamRamDelSubDep3Table in-memory RAM DbIO implementation
 *	for DelSubDep3.
 */
public class CFBamRamDelSubDep3Table
	implements ICFBamDelSubDep3Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamDelSubDep3Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamDelSubDep3Buff >();
	private Map< CFBamDelSubDep3ByDelSubDep2IdxKey,
				Map< CFBamScopePKey,
					CFBamDelSubDep3Buff >> dictByDelSubDep2Idx
		= new HashMap< CFBamDelSubDep3ByDelSubDep2IdxKey,
				Map< CFBamScopePKey,
					CFBamDelSubDep3Buff >>();
	private Map< CFBamDelSubDep3ByUNameIdxKey,
			CFBamDelSubDep3Buff > dictByUNameIdx
		= new HashMap< CFBamDelSubDep3ByUNameIdxKey,
			CFBamDelSubDep3Buff >();

	public CFBamRamDelSubDep3Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createDelSubDep3( CFSecAuthorization Authorization,
		CFBamDelSubDep3Buff Buff )
	{
		final String S_ProcName = "createDelSubDep3";
		schema.getTableDelDep().createDelDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelSubDep3ByDelSubDep2IdxKey keyDelSubDep2Idx = schema.getFactoryDelSubDep3().newDelSubDep2IdxKey();
		keyDelSubDep2Idx.setRequiredDelSubDep2TenantId( Buff.getRequiredDelSubDep2TenantId() );
		keyDelSubDep2Idx.setRequiredDelSubDep2Id( Buff.getRequiredDelSubDep2Id() );

		CFBamDelSubDep3ByUNameIdxKey keyUNameIdx = schema.getFactoryDelSubDep3().newUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep2TenantId( Buff.getRequiredDelSubDep2TenantId() );
		keyUNameIdx.setRequiredDelSubDep2Id( Buff.getRequiredDelSubDep2Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelSubDep3UNameIdx",
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
				if( null == schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep2TenantId(),
						Buff.getRequiredDelSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"DelSubDep2",
						"DelSubDep2",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamDelSubDep3Buff > subdictDelSubDep2Idx;
		if( dictByDelSubDep2Idx.containsKey( keyDelSubDep2Idx ) ) {
			subdictDelSubDep2Idx = dictByDelSubDep2Idx.get( keyDelSubDep2Idx );
		}
		else {
			subdictDelSubDep2Idx = new HashMap< CFBamScopePKey, CFBamDelSubDep3Buff >();
			dictByDelSubDep2Idx.put( keyDelSubDep2Idx, subdictDelSubDep2Idx );
		}
		subdictDelSubDep2Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamDelSubDep3Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelSubDep3Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep3Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelSubDep3Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep3Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelSubDep3.readAllDerived";
		CFBamDelSubDep3Buff[] retList = new CFBamDelSubDep3Buff[ dictByPKey.values().size() ];
		Iterator< CFBamDelSubDep3Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamDelSubDep3Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamDelSubDep3Buff> filteredList = new ArrayList<CFBamDelSubDep3Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelSubDep3Buff ) ) {
					filteredList.add( (CFBamDelSubDep3Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelSubDep3Buff[0] ) );
		}
	}

	public CFBamDelSubDep3Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamDelSubDep3Buff> filteredList = new ArrayList<CFBamDelSubDep3Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelSubDep3Buff ) ) {
					filteredList.add( (CFBamDelSubDep3Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelSubDep3Buff[0] ) );
		}
	}

	public CFBamDelSubDep3Buff[] readDerivedByDelDepIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamDelSubDep3Buff> filteredList = new ArrayList<CFBamDelSubDep3Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelSubDep3Buff ) ) {
					filteredList.add( (CFBamDelSubDep3Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelSubDep3Buff[0] ) );
		}
	}

	public CFBamDelSubDep3Buff[] readDerivedByDelSubDep2Idx( CFSecAuthorization Authorization,
		long DelSubDep2TenantId,
		long DelSubDep2Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readDerivedByDelSubDep2Idx";
		CFBamDelSubDep3ByDelSubDep2IdxKey key = schema.getFactoryDelSubDep3().newDelSubDep2IdxKey();
		key.setRequiredDelSubDep2TenantId( DelSubDep2TenantId );
		key.setRequiredDelSubDep2Id( DelSubDep2Id );

		CFBamDelSubDep3Buff[] recArray;
		if( dictByDelSubDep2Idx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamDelSubDep3Buff > subdictDelSubDep2Idx
				= dictByDelSubDep2Idx.get( key );
			recArray = new CFBamDelSubDep3Buff[ subdictDelSubDep2Idx.size() ];
			Iterator< CFBamDelSubDep3Buff > iter = subdictDelSubDep2Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamDelSubDep3Buff > subdictDelSubDep2Idx
				= new HashMap< CFBamScopePKey, CFBamDelSubDep3Buff >();
			dictByDelSubDep2Idx.put( key, subdictDelSubDep2Idx );
			recArray = new CFBamDelSubDep3Buff[0];
		}
		return( recArray );
	}

	public CFBamDelSubDep3Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long DelSubDep2TenantId,
		long DelSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readDerivedByUNameIdx";
		CFBamDelSubDep3ByUNameIdxKey key = schema.getFactoryDelSubDep3().newUNameIdxKey();
		key.setRequiredDelSubDep2TenantId( DelSubDep2TenantId );
		key.setRequiredDelSubDep2Id( DelSubDep2Id );
		key.setRequiredName( Name );

		CFBamDelSubDep3Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep3Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamDelSubDep3Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep3Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readBuff";
		CFBamDelSubDep3Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a81a" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep3Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamDelSubDep3Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a81a" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep3Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readAllBuff";
		CFBamDelSubDep3Buff buff;
		ArrayList<CFBamDelSubDep3Buff> filteredList = new ArrayList<CFBamDelSubDep3Buff>();
		CFBamDelSubDep3Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81a" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep3Buff[0] ) );
	}

	public CFBamDelSubDep3Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamDelSubDep3Buff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamDelSubDep3Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamDelSubDep3Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamDelSubDep3Buff buff;
		ArrayList<CFBamDelSubDep3Buff> filteredList = new ArrayList<CFBamDelSubDep3Buff>();
		CFBamDelSubDep3Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamDelSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep3Buff[0] ) );
	}

	public CFBamDelSubDep3Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDefSchemaIdx() ";
		CFBamDelSubDep3Buff buff;
		ArrayList<CFBamDelSubDep3Buff> filteredList = new ArrayList<CFBamDelSubDep3Buff>();
		CFBamDelSubDep3Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep3Buff[0] ) );
	}

	public CFBamDelSubDep3Buff[] readBuffByDelDepIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDelDepIdx() ";
		CFBamDelSubDep3Buff buff;
		ArrayList<CFBamDelSubDep3Buff> filteredList = new ArrayList<CFBamDelSubDep3Buff>();
		CFBamDelSubDep3Buff[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationTenantId,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep3Buff[0] ) );
	}

	public CFBamDelSubDep3Buff[] readBuffByDelSubDep2Idx( CFSecAuthorization Authorization,
		long DelSubDep2TenantId,
		long DelSubDep2Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readBuffByDelSubDep2Idx() ";
		CFBamDelSubDep3Buff buff;
		ArrayList<CFBamDelSubDep3Buff> filteredList = new ArrayList<CFBamDelSubDep3Buff>();
		CFBamDelSubDep3Buff[] buffList = readDerivedByDelSubDep2Idx( Authorization,
			DelSubDep2TenantId,
			DelSubDep2Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81a" ) ) {
				filteredList.add( (CFBamDelSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep3Buff[0] ) );
	}

	public CFBamDelSubDep3Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long DelSubDep2TenantId,
		long DelSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readBuffByUNameIdx() ";
		CFBamDelSubDep3Buff buff = readDerivedByUNameIdx( Authorization,
			DelSubDep2TenantId,
			DelSubDep2Id,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a81a" ) ) {
			return( (CFBamDelSubDep3Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific DelSubDep3 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDefSchemaTenantId	The DelSubDep3 key attribute of the instance generating the id.
	 *
	 *	@param	argDefSchemaId	The DelSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelSubDep3Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelSubDep3 buffer instances identified by the duplicate key DelDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argRelationTenantId	The DelSubDep3 key attribute of the instance generating the id.
	 *
	 *	@param	argRelationId	The DelSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelSubDep3Buff[] pageBuffByDelDepIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDelDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelSubDep3 buffer instances identified by the duplicate key DelSubDep2Idx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDelSubDep2TenantId	The DelSubDep3 key attribute of the instance generating the id.
	 *
	 *	@param	argDelSubDep2Id	The DelSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelSubDep3Buff[] pageBuffByDelSubDep2Idx( CFSecAuthorization Authorization,
		long DelSubDep2TenantId,
		long DelSubDep2Id,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDelSubDep2Idx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateDelSubDep3( CFSecAuthorization Authorization,
		CFBamDelSubDep3Buff Buff )
	{
		schema.getTableDelDep().updateDelDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelSubDep3Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelSubDep3",
				"Existing record not found",
				"DelSubDep3",
				pkey );
		}
		CFBamDelSubDep3ByDelSubDep2IdxKey existingKeyDelSubDep2Idx = schema.getFactoryDelSubDep3().newDelSubDep2IdxKey();
		existingKeyDelSubDep2Idx.setRequiredDelSubDep2TenantId( existing.getRequiredDelSubDep2TenantId() );
		existingKeyDelSubDep2Idx.setRequiredDelSubDep2Id( existing.getRequiredDelSubDep2Id() );

		CFBamDelSubDep3ByDelSubDep2IdxKey newKeyDelSubDep2Idx = schema.getFactoryDelSubDep3().newDelSubDep2IdxKey();
		newKeyDelSubDep2Idx.setRequiredDelSubDep2TenantId( Buff.getRequiredDelSubDep2TenantId() );
		newKeyDelSubDep2Idx.setRequiredDelSubDep2Id( Buff.getRequiredDelSubDep2Id() );

		CFBamDelSubDep3ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryDelSubDep3().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredDelSubDep2TenantId( existing.getRequiredDelSubDep2TenantId() );
		existingKeyUNameIdx.setRequiredDelSubDep2Id( existing.getRequiredDelSubDep2Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamDelSubDep3ByUNameIdxKey newKeyUNameIdx = schema.getFactoryDelSubDep3().newUNameIdxKey();
		newKeyUNameIdx.setRequiredDelSubDep2TenantId( Buff.getRequiredDelSubDep2TenantId() );
		newKeyUNameIdx.setRequiredDelSubDep2Id( Buff.getRequiredDelSubDep2Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelSubDep3",
					"DelSubDep3UNameIdx",
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
						"updateDelSubDep3",
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
				if( null == schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep2TenantId(),
						Buff.getRequiredDelSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep3",
						"Container",
						"DelSubDep2",
						"DelSubDep2",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamDelSubDep3Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByDelSubDep2Idx.get( existingKeyDelSubDep2Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDelSubDep2Idx.containsKey( newKeyDelSubDep2Idx ) ) {
			subdict = dictByDelSubDep2Idx.get( newKeyDelSubDep2Idx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamDelSubDep3Buff >();
			dictByDelSubDep2Idx.put( newKeyDelSubDep2Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deleteDelSubDep3( CFSecAuthorization Authorization,
		CFBamDelSubDep3Buff Buff )
	{
		final String S_ProcName = "CFBamRamDelSubDep3Table.deleteDelSubDep3() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelSubDep3Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelSubDep3",
				pkey );
		}
		CFBamDelSubDep3ByDelSubDep2IdxKey keyDelSubDep2Idx = schema.getFactoryDelSubDep3().newDelSubDep2IdxKey();
		keyDelSubDep2Idx.setRequiredDelSubDep2TenantId( existing.getRequiredDelSubDep2TenantId() );
		keyDelSubDep2Idx.setRequiredDelSubDep2Id( existing.getRequiredDelSubDep2Id() );

		CFBamDelSubDep3ByUNameIdxKey keyUNameIdx = schema.getFactoryDelSubDep3().newUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep2TenantId( existing.getRequiredDelSubDep2TenantId() );
		keyUNameIdx.setRequiredDelSubDep2Id( existing.getRequiredDelSubDep2Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamDelSubDep3Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByDelSubDep2Idx.get( keyDelSubDep2Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableDelDep().deleteDelDep( Authorization,
			Buff );
	}
	public void deleteDelSubDep3ByDelSubDep2Idx( CFSecAuthorization Authorization,
		long argDelSubDep2TenantId,
		long argDelSubDep2Id )
	{
		CFBamDelSubDep3ByDelSubDep2IdxKey key = schema.getFactoryDelSubDep3().newDelSubDep2IdxKey();
		key.setRequiredDelSubDep2TenantId( argDelSubDep2TenantId );
		key.setRequiredDelSubDep2Id( argDelSubDep2Id );
		deleteDelSubDep3ByDelSubDep2Idx( Authorization, key );
	}

	public void deleteDelSubDep3ByDelSubDep2Idx( CFSecAuthorization Authorization,
		CFBamDelSubDep3ByDelSubDep2IdxKey argKey )
	{
		CFBamDelSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep3Buff> matchSet = new LinkedList<CFBamDelSubDep3Buff>();
		Iterator<CFBamDelSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep3( Authorization, cur );
		}
	}

	public void deleteDelSubDep3ByUNameIdx( CFSecAuthorization Authorization,
		long argDelSubDep2TenantId,
		long argDelSubDep2Id,
		String argName )
	{
		CFBamDelSubDep3ByUNameIdxKey key = schema.getFactoryDelSubDep3().newUNameIdxKey();
		key.setRequiredDelSubDep2TenantId( argDelSubDep2TenantId );
		key.setRequiredDelSubDep2Id( argDelSubDep2Id );
		key.setRequiredName( argName );
		deleteDelSubDep3ByUNameIdx( Authorization, key );
	}

	public void deleteDelSubDep3ByUNameIdx( CFSecAuthorization Authorization,
		CFBamDelSubDep3ByUNameIdxKey argKey )
	{
		CFBamDelSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep3Buff> matchSet = new LinkedList<CFBamDelSubDep3Buff>();
		Iterator<CFBamDelSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep3( Authorization, cur );
		}
	}

	public void deleteDelSubDep3ByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamDelDepByDefSchemaIdxKey key = schema.getFactoryDelDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelSubDep3ByDefSchemaIdx( Authorization, key );
	}

	public void deleteDelSubDep3ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamDelSubDep3Buff cur;
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
		LinkedList<CFBamDelSubDep3Buff> matchSet = new LinkedList<CFBamDelSubDep3Buff>();
		Iterator<CFBamDelSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep3( Authorization, cur );
		}
	}

	public void deleteDelSubDep3ByDelDepIdx( CFSecAuthorization Authorization,
		long argRelationTenantId,
		long argRelationId )
	{
		CFBamDelDepByDelDepIdxKey key = schema.getFactoryDelDep().newDelDepIdxKey();
		key.setRequiredRelationTenantId( argRelationTenantId );
		key.setRequiredRelationId( argRelationId );
		deleteDelSubDep3ByDelDepIdx( Authorization, key );
	}

	public void deleteDelSubDep3ByDelDepIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamDelSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep3Buff> matchSet = new LinkedList<CFBamDelSubDep3Buff>();
		Iterator<CFBamDelSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep3( Authorization, cur );
		}
	}

	public void deleteDelSubDep3ByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteDelSubDep3ByIdIdx( Authorization, key );
	}

	public void deleteDelSubDep3ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamDelSubDep3Buff cur;
		LinkedList<CFBamDelSubDep3Buff> matchSet = new LinkedList<CFBamDelSubDep3Buff>();
		Iterator<CFBamDelSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep3( Authorization, cur );
		}
	}

	public void deleteDelSubDep3ByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelSubDep3ByTenantIdx( Authorization, key );
	}

	public void deleteDelSubDep3ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamDelSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep3Buff> matchSet = new LinkedList<CFBamDelSubDep3Buff>();
		Iterator<CFBamDelSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep3( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
