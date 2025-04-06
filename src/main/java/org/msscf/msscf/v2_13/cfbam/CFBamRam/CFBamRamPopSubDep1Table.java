
// Description: Java 11 in-memory RAM DbIO implementation for PopSubDep1.

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
 *	CFBamRamPopSubDep1Table in-memory RAM DbIO implementation
 *	for PopSubDep1.
 */
public class CFBamRamPopSubDep1Table
	implements ICFBamPopSubDep1Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamPopSubDep1Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamPopSubDep1Buff >();
	private Map< CFBamPopSubDep1ByPopTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamPopSubDep1Buff >> dictByPopTopDepIdx
		= new HashMap< CFBamPopSubDep1ByPopTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamPopSubDep1Buff >>();
	private Map< CFBamPopSubDep1ByUNameIdxKey,
			CFBamPopSubDep1Buff > dictByUNameIdx
		= new HashMap< CFBamPopSubDep1ByUNameIdxKey,
			CFBamPopSubDep1Buff >();

	public CFBamRamPopSubDep1Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createPopSubDep1( CFSecAuthorization Authorization,
		CFBamPopSubDep1Buff Buff )
	{
		final String S_ProcName = "createPopSubDep1";
		schema.getTablePopDep().createPopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep1ByPopTopDepIdxKey keyPopTopDepIdx = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		keyPopTopDepIdx.setRequiredPopTopDepTenantId( Buff.getRequiredPopTopDepTenantId() );
		keyPopTopDepIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );

		CFBamPopSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryPopSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredPopTopDepTenantId( Buff.getRequiredPopTopDepTenantId() );
		keyUNameIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopSubDep1UNameIdx",
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
				if( null == schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopTopDepTenantId(),
						Buff.getRequiredPopTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"PopTopDep",
						"PopTopDep",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdictPopTopDepIdx;
		if( dictByPopTopDepIdx.containsKey( keyPopTopDepIdx ) ) {
			subdictPopTopDepIdx = dictByPopTopDepIdx.get( keyPopTopDepIdx );
		}
		else {
			subdictPopTopDepIdx = new HashMap< CFBamScopePKey, CFBamPopSubDep1Buff >();
			dictByPopTopDepIdx.put( keyPopTopDepIdx, subdictPopTopDepIdx );
		}
		subdictPopTopDepIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamPopSubDep1Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopSubDep1.readAllDerived";
		CFBamPopSubDep1Buff[] retList = new CFBamPopSubDep1Buff[ dictByPKey.values().size() ];
		Iterator< CFBamPopSubDep1Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamPopSubDep1Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep1Buff ) ) {
					filteredList.add( (CFBamPopSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
		}
	}

	public CFBamPopSubDep1Buff[] readDerivedByRelationIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep1Buff ) ) {
					filteredList.add( (CFBamPopSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
		}
	}

	public CFBamPopSubDep1Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep1Buff ) ) {
					filteredList.add( (CFBamPopSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
		}
	}

	public CFBamPopSubDep1Buff[] readDerivedByPopTopDepIdx( CFSecAuthorization Authorization,
		long PopTopDepTenantId,
		long PopTopDepId )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerivedByPopTopDepIdx";
		CFBamPopSubDep1ByPopTopDepIdxKey key = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		key.setRequiredPopTopDepTenantId( PopTopDepTenantId );
		key.setRequiredPopTopDepId( PopTopDepId );

		CFBamPopSubDep1Buff[] recArray;
		if( dictByPopTopDepIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdictPopTopDepIdx
				= dictByPopTopDepIdx.get( key );
			recArray = new CFBamPopSubDep1Buff[ subdictPopTopDepIdx.size() ];
			Iterator< CFBamPopSubDep1Buff > iter = subdictPopTopDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdictPopTopDepIdx
				= new HashMap< CFBamScopePKey, CFBamPopSubDep1Buff >();
			dictByPopTopDepIdx.put( key, subdictPopTopDepIdx );
			recArray = new CFBamPopSubDep1Buff[0];
		}
		return( recArray );
	}

	public CFBamPopSubDep1Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long PopTopDepTenantId,
		long PopTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerivedByUNameIdx";
		CFBamPopSubDep1ByUNameIdxKey key = schema.getFactoryPopSubDep1().newUNameIdxKey();
		key.setRequiredPopTopDepTenantId( PopTopDepTenantId );
		key.setRequiredPopTopDepId( PopTopDepId );
		key.setRequiredName( Name );

		CFBamPopSubDep1Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamPopSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readBuff";
		CFBamPopSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a831" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamPopSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a831" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep1Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readAllBuff";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a831" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamPopSubDep1Buff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamPopSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamPopSubDep1Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamPopSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff[] readBuffByRelationIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByRelationIdx() ";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readDerivedByRelationIdx( Authorization,
			RelationTenantId,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByDefSchemaIdx() ";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff[] readBuffByPopTopDepIdx( CFSecAuthorization Authorization,
		long PopTopDepTenantId,
		long PopTopDepId )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readBuffByPopTopDepIdx() ";
		CFBamPopSubDep1Buff buff;
		ArrayList<CFBamPopSubDep1Buff> filteredList = new ArrayList<CFBamPopSubDep1Buff>();
		CFBamPopSubDep1Buff[] buffList = readDerivedByPopTopDepIdx( Authorization,
			PopTopDepTenantId,
			PopTopDepId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a831" ) ) {
				filteredList.add( (CFBamPopSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep1Buff[0] ) );
	}

	public CFBamPopSubDep1Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long PopTopDepTenantId,
		long PopTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readBuffByUNameIdx() ";
		CFBamPopSubDep1Buff buff = readDerivedByUNameIdx( Authorization,
			PopTopDepTenantId,
			PopTopDepId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a831" ) ) {
			return( (CFBamPopSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific PopSubDep1 buffer instances identified by the duplicate key RelationIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argRelationTenantId	The PopSubDep1 key attribute of the instance generating the id.
	 *
	 *	@param	argRelationId	The PopSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep1Buff[] pageBuffByRelationIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByRelationIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep1 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDefSchemaTenantId	The PopSubDep1 key attribute of the instance generating the id.
	 *
	 *	@param	argDefSchemaId	The PopSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep1Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep1 buffer instances identified by the duplicate key PopTopDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argPopTopDepTenantId	The PopSubDep1 key attribute of the instance generating the id.
	 *
	 *	@param	argPopTopDepId	The PopSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep1Buff[] pageBuffByPopTopDepIdx( CFSecAuthorization Authorization,
		long PopTopDepTenantId,
		long PopTopDepId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByPopTopDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updatePopSubDep1( CFSecAuthorization Authorization,
		CFBamPopSubDep1Buff Buff )
	{
		schema.getTablePopDep().updatePopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopSubDep1",
				"Existing record not found",
				"PopSubDep1",
				pkey );
		}
		CFBamPopSubDep1ByPopTopDepIdxKey existingKeyPopTopDepIdx = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		existingKeyPopTopDepIdx.setRequiredPopTopDepTenantId( existing.getRequiredPopTopDepTenantId() );
		existingKeyPopTopDepIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );

		CFBamPopSubDep1ByPopTopDepIdxKey newKeyPopTopDepIdx = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		newKeyPopTopDepIdx.setRequiredPopTopDepTenantId( Buff.getRequiredPopTopDepTenantId() );
		newKeyPopTopDepIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );

		CFBamPopSubDep1ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryPopSubDep1().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredPopTopDepTenantId( existing.getRequiredPopTopDepTenantId() );
		existingKeyUNameIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamPopSubDep1ByUNameIdxKey newKeyUNameIdx = schema.getFactoryPopSubDep1().newUNameIdxKey();
		newKeyUNameIdx.setRequiredPopTopDepTenantId( Buff.getRequiredPopTopDepTenantId() );
		newKeyUNameIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopSubDep1",
					"PopSubDep1UNameIdx",
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
						"updatePopSubDep1",
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
				if( null == schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopTopDepTenantId(),
						Buff.getRequiredPopTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep1",
						"Container",
						"PopTopDep",
						"PopTopDep",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByPopTopDepIdx.get( existingKeyPopTopDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPopTopDepIdx.containsKey( newKeyPopTopDepIdx ) ) {
			subdict = dictByPopTopDepIdx.get( newKeyPopTopDepIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamPopSubDep1Buff >();
			dictByPopTopDepIdx.put( newKeyPopTopDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deletePopSubDep1( CFSecAuthorization Authorization,
		CFBamPopSubDep1Buff Buff )
	{
		final String S_ProcName = "CFBamRamPopSubDep1Table.deletePopSubDep1() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopSubDep1",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckPopDep[] = schema.getTablePopSubDep2().readDerivedByPopSubDep1Idx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		if( arrCheckPopDep.length > 0 ) {
			schema.getTablePopSubDep2().deletePopSubDep2ByPopSubDep1Idx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		}
		CFBamPopSubDep1ByPopTopDepIdxKey keyPopTopDepIdx = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		keyPopTopDepIdx.setRequiredPopTopDepTenantId( existing.getRequiredPopTopDepTenantId() );
		keyPopTopDepIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );

		CFBamPopSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryPopSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredPopTopDepTenantId( existing.getRequiredPopTopDepTenantId() );
		keyUNameIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamPopSubDep1Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByPopTopDepIdx.get( keyPopTopDepIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	public void deletePopSubDep1ByPopTopDepIdx( CFSecAuthorization Authorization,
		long argPopTopDepTenantId,
		long argPopTopDepId )
	{
		CFBamPopSubDep1ByPopTopDepIdxKey key = schema.getFactoryPopSubDep1().newPopTopDepIdxKey();
		key.setRequiredPopTopDepTenantId( argPopTopDepTenantId );
		key.setRequiredPopTopDepId( argPopTopDepId );
		deletePopSubDep1ByPopTopDepIdx( Authorization, key );
	}

	public void deletePopSubDep1ByPopTopDepIdx( CFSecAuthorization Authorization,
		CFBamPopSubDep1ByPopTopDepIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		long argPopTopDepTenantId,
		long argPopTopDepId,
		String argName )
	{
		CFBamPopSubDep1ByUNameIdxKey key = schema.getFactoryPopSubDep1().newUNameIdxKey();
		key.setRequiredPopTopDepTenantId( argPopTopDepTenantId );
		key.setRequiredPopTopDepId( argPopTopDepId );
		key.setRequiredName( argName );
		deletePopSubDep1ByUNameIdx( Authorization, key );
	}

	public void deletePopSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		CFBamPopSubDep1ByUNameIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByRelationIdx( CFSecAuthorization Authorization,
		long argRelationTenantId,
		long argRelationId )
	{
		CFBamPopDepByRelationIdxKey key = schema.getFactoryPopDep().newRelationIdxKey();
		key.setRequiredRelationTenantId( argRelationTenantId );
		key.setRequiredRelationId( argRelationId );
		deletePopSubDep1ByRelationIdx( Authorization, key );
	}

	public void deletePopSubDep1ByRelationIdx( CFSecAuthorization Authorization,
		CFBamPopDepByRelationIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamPopDepByDefSchemaIdxKey key = schema.getFactoryPopDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopSubDep1ByDefSchemaIdx( Authorization, key );
	}

	public void deletePopSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamPopDepByDefSchemaIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
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
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deletePopSubDep1ByIdIdx( Authorization, key );
	}

	public void deletePopSubDep1ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamPopSubDep1Buff cur;
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void deletePopSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopSubDep1ByTenantIdx( Authorization, key );
	}

	public void deletePopSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamPopSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep1Buff> matchSet = new LinkedList<CFBamPopSubDep1Buff>();
		Iterator<CFBamPopSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deletePopSubDep1( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
