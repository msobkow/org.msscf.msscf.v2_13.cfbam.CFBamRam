
// Description: Java 11 in-memory RAM DbIO implementation for PopSubDep3.

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
 *	CFBamRamPopSubDep3Table in-memory RAM DbIO implementation
 *	for PopSubDep3.
 */
public class CFBamRamPopSubDep3Table
	implements ICFBamPopSubDep3Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamPopSubDep3Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamPopSubDep3Buff >();
	private Map< CFBamPopSubDep3ByPopSubDep2IdxKey,
				Map< CFBamScopePKey,
					CFBamPopSubDep3Buff >> dictByPopSubDep2Idx
		= new HashMap< CFBamPopSubDep3ByPopSubDep2IdxKey,
				Map< CFBamScopePKey,
					CFBamPopSubDep3Buff >>();
	private Map< CFBamPopSubDep3ByUNameIdxKey,
			CFBamPopSubDep3Buff > dictByUNameIdx
		= new HashMap< CFBamPopSubDep3ByUNameIdxKey,
			CFBamPopSubDep3Buff >();

	public CFBamRamPopSubDep3Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createPopSubDep3( CFSecAuthorization Authorization,
		CFBamPopSubDep3Buff Buff )
	{
		final String S_ProcName = "createPopSubDep3";
		schema.getTablePopDep().createPopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep3ByPopSubDep2IdxKey keyPopSubDep2Idx = schema.getFactoryPopSubDep3().newPopSubDep2IdxKey();
		keyPopSubDep2Idx.setRequiredPopSubDep2TenantId( Buff.getRequiredPopSubDep2TenantId() );
		keyPopSubDep2Idx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );

		CFBamPopSubDep3ByUNameIdxKey keyUNameIdx = schema.getFactoryPopSubDep3().newUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep2TenantId( Buff.getRequiredPopSubDep2TenantId() );
		keyUNameIdx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopSubDep3UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep2TenantId(),
						Buff.getRequiredPopSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"PopSubDep2",
						"PopSubDep2",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamPopSubDep3Buff > subdictPopSubDep2Idx;
		if( dictByPopSubDep2Idx.containsKey( keyPopSubDep2Idx ) ) {
			subdictPopSubDep2Idx = dictByPopSubDep2Idx.get( keyPopSubDep2Idx );
		}
		else {
			subdictPopSubDep2Idx = new HashMap< CFBamScopePKey, CFBamPopSubDep3Buff >();
			dictByPopSubDep2Idx.put( keyPopSubDep2Idx, subdictPopSubDep2Idx );
		}
		subdictPopSubDep2Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamPopSubDep3Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopSubDep3Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep3Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopSubDep3Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep3Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopSubDep3.readAllDerived";
		CFBamPopSubDep3Buff[] retList = new CFBamPopSubDep3Buff[ dictByPKey.values().size() ];
		Iterator< CFBamPopSubDep3Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamPopSubDep3Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopSubDep3Buff> filteredList = new ArrayList<CFBamPopSubDep3Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep3Buff ) ) {
					filteredList.add( (CFBamPopSubDep3Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep3Buff[0] ) );
		}
	}

	public CFBamPopSubDep3Buff[] readDerivedByRelationIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByRelationIdx";
		CFBamPopDepBuff buffList[] = schema.getTablePopDep().readDerivedByRelationIdx( Authorization,
			RelationTenantId,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamPopDepBuff buff;
			ArrayList<CFBamPopSubDep3Buff> filteredList = new ArrayList<CFBamPopSubDep3Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep3Buff ) ) {
					filteredList.add( (CFBamPopSubDep3Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep3Buff[0] ) );
		}
	}

	public CFBamPopSubDep3Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByDefSchemaIdx";
		CFBamPopDepBuff buffList[] = schema.getTablePopDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamPopDepBuff buff;
			ArrayList<CFBamPopSubDep3Buff> filteredList = new ArrayList<CFBamPopSubDep3Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep3Buff ) ) {
					filteredList.add( (CFBamPopSubDep3Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep3Buff[0] ) );
		}
	}

	public CFBamPopSubDep3Buff[] readDerivedByPopSubDep2Idx( CFSecAuthorization Authorization,
		long PopSubDep2TenantId,
		long PopSubDep2Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerivedByPopSubDep2Idx";
		CFBamPopSubDep3ByPopSubDep2IdxKey key = schema.getFactoryPopSubDep3().newPopSubDep2IdxKey();
		key.setRequiredPopSubDep2TenantId( PopSubDep2TenantId );
		key.setRequiredPopSubDep2Id( PopSubDep2Id );

		CFBamPopSubDep3Buff[] recArray;
		if( dictByPopSubDep2Idx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamPopSubDep3Buff > subdictPopSubDep2Idx
				= dictByPopSubDep2Idx.get( key );
			recArray = new CFBamPopSubDep3Buff[ subdictPopSubDep2Idx.size() ];
			Iterator< CFBamPopSubDep3Buff > iter = subdictPopSubDep2Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamPopSubDep3Buff > subdictPopSubDep2Idx
				= new HashMap< CFBamScopePKey, CFBamPopSubDep3Buff >();
			dictByPopSubDep2Idx.put( key, subdictPopSubDep2Idx );
			recArray = new CFBamPopSubDep3Buff[0];
		}
		return( recArray );
	}

	public CFBamPopSubDep3Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long PopSubDep2TenantId,
		long PopSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerivedByUNameIdx";
		CFBamPopSubDep3ByUNameIdxKey key = schema.getFactoryPopSubDep3().newUNameIdxKey();
		key.setRequiredPopSubDep2TenantId( PopSubDep2TenantId );
		key.setRequiredPopSubDep2Id( PopSubDep2Id );
		key.setRequiredName( Name );

		CFBamPopSubDep3Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep3Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamPopSubDep3Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep3Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readBuff";
		CFBamPopSubDep3Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a833" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep3Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamPopSubDep3Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a833" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep3Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readAllBuff";
		CFBamPopSubDep3Buff buff;
		ArrayList<CFBamPopSubDep3Buff> filteredList = new ArrayList<CFBamPopSubDep3Buff>();
		CFBamPopSubDep3Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a833" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep3Buff[0] ) );
	}

	public CFBamPopSubDep3Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamPopSubDep3Buff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamPopSubDep3Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamPopSubDep3Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamPopSubDep3Buff buff;
		ArrayList<CFBamPopSubDep3Buff> filteredList = new ArrayList<CFBamPopSubDep3Buff>();
		CFBamPopSubDep3Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamPopSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep3Buff[0] ) );
	}

	public CFBamPopSubDep3Buff[] readBuffByRelationIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByRelationIdx() ";
		CFBamPopSubDep3Buff buff;
		ArrayList<CFBamPopSubDep3Buff> filteredList = new ArrayList<CFBamPopSubDep3Buff>();
		CFBamPopSubDep3Buff[] buffList = readDerivedByRelationIdx( Authorization,
			RelationTenantId,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep3Buff[0] ) );
	}

	public CFBamPopSubDep3Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByDefSchemaIdx() ";
		CFBamPopSubDep3Buff buff;
		ArrayList<CFBamPopSubDep3Buff> filteredList = new ArrayList<CFBamPopSubDep3Buff>();
		CFBamPopSubDep3Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep3Buff[0] ) );
	}

	public CFBamPopSubDep3Buff[] readBuffByPopSubDep2Idx( CFSecAuthorization Authorization,
		long PopSubDep2TenantId,
		long PopSubDep2Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readBuffByPopSubDep2Idx() ";
		CFBamPopSubDep3Buff buff;
		ArrayList<CFBamPopSubDep3Buff> filteredList = new ArrayList<CFBamPopSubDep3Buff>();
		CFBamPopSubDep3Buff[] buffList = readDerivedByPopSubDep2Idx( Authorization,
			PopSubDep2TenantId,
			PopSubDep2Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a833" ) ) {
				filteredList.add( (CFBamPopSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep3Buff[0] ) );
	}

	public CFBamPopSubDep3Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long PopSubDep2TenantId,
		long PopSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readBuffByUNameIdx() ";
		CFBamPopSubDep3Buff buff = readDerivedByUNameIdx( Authorization,
			PopSubDep2TenantId,
			PopSubDep2Id,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a833" ) ) {
			return( (CFBamPopSubDep3Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific PopSubDep3 buffer instances identified by the duplicate key RelationIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argRelationTenantId	The PopSubDep3 key attribute of the instance generating the id.
	 *
	 *	@param	argRelationId	The PopSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep3Buff[] pageBuffByRelationIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByRelationIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep3 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDefSchemaTenantId	The PopSubDep3 key attribute of the instance generating the id.
	 *
	 *	@param	argDefSchemaId	The PopSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep3Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep3 buffer instances identified by the duplicate key PopSubDep2Idx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argPopSubDep2TenantId	The PopSubDep3 key attribute of the instance generating the id.
	 *
	 *	@param	argPopSubDep2Id	The PopSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep3Buff[] pageBuffByPopSubDep2Idx( CFSecAuthorization Authorization,
		long PopSubDep2TenantId,
		long PopSubDep2Id,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByPopSubDep2Idx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updatePopSubDep3( CFSecAuthorization Authorization,
		CFBamPopSubDep3Buff Buff )
	{
		schema.getTablePopDep().updatePopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep3Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopSubDep3",
				"Existing record not found",
				"PopSubDep3",
				pkey );
		}
		CFBamPopSubDep3ByPopSubDep2IdxKey existingKeyPopSubDep2Idx = schema.getFactoryPopSubDep3().newPopSubDep2IdxKey();
		existingKeyPopSubDep2Idx.setRequiredPopSubDep2TenantId( existing.getRequiredPopSubDep2TenantId() );
		existingKeyPopSubDep2Idx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );

		CFBamPopSubDep3ByPopSubDep2IdxKey newKeyPopSubDep2Idx = schema.getFactoryPopSubDep3().newPopSubDep2IdxKey();
		newKeyPopSubDep2Idx.setRequiredPopSubDep2TenantId( Buff.getRequiredPopSubDep2TenantId() );
		newKeyPopSubDep2Idx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );

		CFBamPopSubDep3ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryPopSubDep3().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredPopSubDep2TenantId( existing.getRequiredPopSubDep2TenantId() );
		existingKeyUNameIdx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamPopSubDep3ByUNameIdxKey newKeyUNameIdx = schema.getFactoryPopSubDep3().newUNameIdxKey();
		newKeyUNameIdx.setRequiredPopSubDep2TenantId( Buff.getRequiredPopSubDep2TenantId() );
		newKeyUNameIdx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopSubDep3",
					"PopSubDep3UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep3",
						"Superclass",
						"SuperClass",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep2TenantId(),
						Buff.getRequiredPopSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep3",
						"Container",
						"PopSubDep2",
						"PopSubDep2",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamPopSubDep3Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByPopSubDep2Idx.get( existingKeyPopSubDep2Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPopSubDep2Idx.containsKey( newKeyPopSubDep2Idx ) ) {
			subdict = dictByPopSubDep2Idx.get( newKeyPopSubDep2Idx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamPopSubDep3Buff >();
			dictByPopSubDep2Idx.put( newKeyPopSubDep2Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deletePopSubDep3( CFSecAuthorization Authorization,
		CFBamPopSubDep3Buff Buff )
	{
		final String S_ProcName = "CFBamRamPopSubDep3Table.deletePopSubDep3() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep3Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopSubDep3",
				pkey );
		}
		CFBamPopSubDep3ByPopSubDep2IdxKey keyPopSubDep2Idx = schema.getFactoryPopSubDep3().newPopSubDep2IdxKey();
		keyPopSubDep2Idx.setRequiredPopSubDep2TenantId( existing.getRequiredPopSubDep2TenantId() );
		keyPopSubDep2Idx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );

		CFBamPopSubDep3ByUNameIdxKey keyUNameIdx = schema.getFactoryPopSubDep3().newUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep2TenantId( existing.getRequiredPopSubDep2TenantId() );
		keyUNameIdx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamPopSubDep3Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByPopSubDep2Idx.get( keyPopSubDep2Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	public void deletePopSubDep3ByPopSubDep2Idx( CFSecAuthorization Authorization,
		long argPopSubDep2TenantId,
		long argPopSubDep2Id )
	{
		CFBamPopSubDep3ByPopSubDep2IdxKey key = schema.getFactoryPopSubDep3().newPopSubDep2IdxKey();
		key.setRequiredPopSubDep2TenantId( argPopSubDep2TenantId );
		key.setRequiredPopSubDep2Id( argPopSubDep2Id );
		deletePopSubDep3ByPopSubDep2Idx( Authorization, key );
	}

	public void deletePopSubDep3ByPopSubDep2Idx( CFSecAuthorization Authorization,
		CFBamPopSubDep3ByPopSubDep2IdxKey argKey )
	{
		CFBamPopSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep3Buff> matchSet = new LinkedList<CFBamPopSubDep3Buff>();
		Iterator<CFBamPopSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByUNameIdx( CFSecAuthorization Authorization,
		long argPopSubDep2TenantId,
		long argPopSubDep2Id,
		String argName )
	{
		CFBamPopSubDep3ByUNameIdxKey key = schema.getFactoryPopSubDep3().newUNameIdxKey();
		key.setRequiredPopSubDep2TenantId( argPopSubDep2TenantId );
		key.setRequiredPopSubDep2Id( argPopSubDep2Id );
		key.setRequiredName( argName );
		deletePopSubDep3ByUNameIdx( Authorization, key );
	}

	public void deletePopSubDep3ByUNameIdx( CFSecAuthorization Authorization,
		CFBamPopSubDep3ByUNameIdxKey argKey )
	{
		CFBamPopSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep3Buff> matchSet = new LinkedList<CFBamPopSubDep3Buff>();
		Iterator<CFBamPopSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByRelationIdx( CFSecAuthorization Authorization,
		long argRelationTenantId,
		long argRelationId )
	{
		CFBamPopDepByRelationIdxKey key = schema.getFactoryPopDep().newRelationIdxKey();
		key.setRequiredRelationTenantId( argRelationTenantId );
		key.setRequiredRelationId( argRelationId );
		deletePopSubDep3ByRelationIdx( Authorization, key );
	}

	public void deletePopSubDep3ByRelationIdx( CFSecAuthorization Authorization,
		CFBamPopDepByRelationIdxKey argKey )
	{
		CFBamPopSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep3Buff> matchSet = new LinkedList<CFBamPopSubDep3Buff>();
		Iterator<CFBamPopSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamPopDepByDefSchemaIdxKey key = schema.getFactoryPopDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopSubDep3ByDefSchemaIdx( Authorization, key );
	}

	public void deletePopSubDep3ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamPopDepByDefSchemaIdxKey argKey )
	{
		CFBamPopSubDep3Buff cur;
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
		LinkedList<CFBamPopSubDep3Buff> matchSet = new LinkedList<CFBamPopSubDep3Buff>();
		Iterator<CFBamPopSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deletePopSubDep3ByIdIdx( Authorization, key );
	}

	public void deletePopSubDep3ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamPopSubDep3Buff cur;
		LinkedList<CFBamPopSubDep3Buff> matchSet = new LinkedList<CFBamPopSubDep3Buff>();
		Iterator<CFBamPopSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void deletePopSubDep3ByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopSubDep3ByTenantIdx( Authorization, key );
	}

	public void deletePopSubDep3ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamPopSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep3Buff> matchSet = new LinkedList<CFBamPopSubDep3Buff>();
		Iterator<CFBamPopSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep3( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
