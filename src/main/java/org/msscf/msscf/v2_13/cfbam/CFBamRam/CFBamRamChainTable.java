
// Description: Java 11 in-memory RAM DbIO implementation for Chain.

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
 *	CFBamRamChainTable in-memory RAM DbIO implementation
 *	for Chain.
 */
public class CFBamRamChainTable
	implements ICFBamChainTable
{
	private ICFBamSchema schema;
	private Map< CFBamChainPKey,
				CFBamChainBuff > dictByPKey
		= new HashMap< CFBamChainPKey,
				CFBamChainBuff >();
	private Map< CFBamChainByTenantIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >> dictByTenantIdx
		= new HashMap< CFBamChainByTenantIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >>();
	private Map< CFBamChainByChainTableIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >> dictByChainTableIdx
		= new HashMap< CFBamChainByChainTableIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >>();
	private Map< CFBamChainByDefSchemaIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamChainByDefSchemaIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >>();
	private Map< CFBamChainByUNameIdxKey,
			CFBamChainBuff > dictByUNameIdx
		= new HashMap< CFBamChainByUNameIdxKey,
			CFBamChainBuff >();
	private Map< CFBamChainByPrevRelIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >> dictByPrevRelIdx
		= new HashMap< CFBamChainByPrevRelIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >>();
	private Map< CFBamChainByNextRelIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >> dictByNextRelIdx
		= new HashMap< CFBamChainByNextRelIdxKey,
				Map< CFBamChainPKey,
					CFBamChainBuff >>();

	public CFBamRamChainTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createChain( CFSecAuthorization Authorization,
		CFBamChainBuff Buff )
	{
		final String S_ProcName = "createChain";
		CFBamChainPKey pkey = schema.getFactoryChain().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( ((CFBamRamTenantTable)schema.getTableTenant()).nextChainIdGen( Authorization,
			Buff.getRequiredTenantId() ) );
		Buff.setRequiredTenantId( pkey.getRequiredTenantId() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamChainByTenantIdxKey keyTenantIdx = schema.getFactoryChain().newTenantIdxKey();
		keyTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamChainByChainTableIdxKey keyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		keyChainTableIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyChainTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamChainByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamChainByUNameIdxKey keyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamChainByPrevRelIdxKey keyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		keyPrevRelIdx.setRequiredPrevRelationTenantId( Buff.getRequiredPrevRelationTenantId() );
		keyPrevRelIdx.setRequiredPrevRelationId( Buff.getRequiredPrevRelationId() );

		CFBamChainByNextRelIdxKey keyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		keyNextRelIdx.setRequiredNextRelationTenantId( Buff.getRequiredNextRelationTenantId() );
		keyNextRelIdx.setRequiredNextRelationId( Buff.getRequiredNextRelationId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ChainUNameIdx",
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

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPrevRelationTenantId(),
						Buff.getRequiredPrevRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"PrevRelation",
						"Relation",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredNextRelationTenantId(),
						Buff.getRequiredNextRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"NextRelation",
						"Relation",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamChainPKey, CFBamChainBuff > subdictTenantIdx;
		if( dictByTenantIdx.containsKey( keyTenantIdx ) ) {
			subdictTenantIdx = dictByTenantIdx.get( keyTenantIdx );
		}
		else {
			subdictTenantIdx = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByTenantIdx.put( keyTenantIdx, subdictTenantIdx );
		}
		subdictTenantIdx.put( pkey, Buff );

		Map< CFBamChainPKey, CFBamChainBuff > subdictChainTableIdx;
		if( dictByChainTableIdx.containsKey( keyChainTableIdx ) ) {
			subdictChainTableIdx = dictByChainTableIdx.get( keyChainTableIdx );
		}
		else {
			subdictChainTableIdx = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByChainTableIdx.put( keyChainTableIdx, subdictChainTableIdx );
		}
		subdictChainTableIdx.put( pkey, Buff );

		Map< CFBamChainPKey, CFBamChainBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamChainPKey, CFBamChainBuff > subdictPrevRelIdx;
		if( dictByPrevRelIdx.containsKey( keyPrevRelIdx ) ) {
			subdictPrevRelIdx = dictByPrevRelIdx.get( keyPrevRelIdx );
		}
		else {
			subdictPrevRelIdx = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByPrevRelIdx.put( keyPrevRelIdx, subdictPrevRelIdx );
		}
		subdictPrevRelIdx.put( pkey, Buff );

		Map< CFBamChainPKey, CFBamChainBuff > subdictNextRelIdx;
		if( dictByNextRelIdx.containsKey( keyNextRelIdx ) ) {
			subdictNextRelIdx = dictByNextRelIdx.get( keyNextRelIdx );
		}
		else {
			subdictNextRelIdx = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByNextRelIdx.put( keyNextRelIdx, subdictNextRelIdx );
		}
		subdictNextRelIdx.put( pkey, Buff );

	}

	public CFBamChainBuff readDerived( CFSecAuthorization Authorization,
		CFBamChainPKey PKey )
	{
		final String S_ProcName = "CFBamRamChain.readDerived";
		CFBamChainPKey key = schema.getFactoryChain().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamChainBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff lockDerived( CFSecAuthorization Authorization,
		CFBamChainPKey PKey )
	{
		final String S_ProcName = "CFBamRamChain.readDerived";
		CFBamChainPKey key = schema.getFactoryChain().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamChainBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamChain.readAllDerived";
		CFBamChainBuff[] retList = new CFBamChainBuff[ dictByPKey.values().size() ];
		Iterator< CFBamChainBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamChainBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByTenantIdx";
		CFBamChainByTenantIdxKey key = schema.getFactoryChain().newTenantIdxKey();
		key.setRequiredTenantId( TenantId );

		CFBamChainBuff[] recArray;
		if( dictByTenantIdx.containsKey( key ) ) {
			Map< CFBamChainPKey, CFBamChainBuff > subdictTenantIdx
				= dictByTenantIdx.get( key );
			recArray = new CFBamChainBuff[ subdictTenantIdx.size() ];
			Iterator< CFBamChainBuff > iter = subdictTenantIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamChainPKey, CFBamChainBuff > subdictTenantIdx
				= new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByTenantIdx.put( key, subdictTenantIdx );
			recArray = new CFBamChainBuff[0];
		}
		return( recArray );
	}

	public CFBamChainBuff[] readDerivedByChainTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByChainTableIdx";
		CFBamChainByChainTableIdxKey key = schema.getFactoryChain().newChainTableIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredTableId( TableId );

		CFBamChainBuff[] recArray;
		if( dictByChainTableIdx.containsKey( key ) ) {
			Map< CFBamChainPKey, CFBamChainBuff > subdictChainTableIdx
				= dictByChainTableIdx.get( key );
			recArray = new CFBamChainBuff[ subdictChainTableIdx.size() ];
			Iterator< CFBamChainBuff > iter = subdictChainTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamChainPKey, CFBamChainBuff > subdictChainTableIdx
				= new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByChainTableIdx.put( key, subdictChainTableIdx );
			recArray = new CFBamChainBuff[0];
		}
		return( recArray );
	}

	public CFBamChainBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByDefSchemaIdx";
		CFBamChainByDefSchemaIdxKey key = schema.getFactoryChain().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( DefSchemaTenantId );
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamChainBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamChainPKey, CFBamChainBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamChainBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamChainBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamChainPKey, CFBamChainBuff > subdictDefSchemaIdx
				= new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamChainBuff[0];
		}
		return( recArray );
	}

	public CFBamChainBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByUNameIdx";
		CFBamChainByUNameIdxKey key = schema.getFactoryChain().newUNameIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		CFBamChainBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff[] readDerivedByPrevRelIdx( CFSecAuthorization Authorization,
		long PrevRelationTenantId,
		long PrevRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByPrevRelIdx";
		CFBamChainByPrevRelIdxKey key = schema.getFactoryChain().newPrevRelIdxKey();
		key.setRequiredPrevRelationTenantId( PrevRelationTenantId );
		key.setRequiredPrevRelationId( PrevRelationId );

		CFBamChainBuff[] recArray;
		if( dictByPrevRelIdx.containsKey( key ) ) {
			Map< CFBamChainPKey, CFBamChainBuff > subdictPrevRelIdx
				= dictByPrevRelIdx.get( key );
			recArray = new CFBamChainBuff[ subdictPrevRelIdx.size() ];
			Iterator< CFBamChainBuff > iter = subdictPrevRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamChainPKey, CFBamChainBuff > subdictPrevRelIdx
				= new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByPrevRelIdx.put( key, subdictPrevRelIdx );
			recArray = new CFBamChainBuff[0];
		}
		return( recArray );
	}

	public CFBamChainBuff[] readDerivedByNextRelIdx( CFSecAuthorization Authorization,
		long NextRelationTenantId,
		long NextRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByNextRelIdx";
		CFBamChainByNextRelIdxKey key = schema.getFactoryChain().newNextRelIdxKey();
		key.setRequiredNextRelationTenantId( NextRelationTenantId );
		key.setRequiredNextRelationId( NextRelationId );

		CFBamChainBuff[] recArray;
		if( dictByNextRelIdx.containsKey( key ) ) {
			Map< CFBamChainPKey, CFBamChainBuff > subdictNextRelIdx
				= dictByNextRelIdx.get( key );
			recArray = new CFBamChainBuff[ subdictNextRelIdx.size() ];
			Iterator< CFBamChainBuff > iter = subdictNextRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamChainPKey, CFBamChainBuff > subdictNextRelIdx
				= new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByNextRelIdx.put( key, subdictNextRelIdx );
			recArray = new CFBamChainBuff[0];
		}
		return( recArray );
	}

	public CFBamChainBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByIdIdx() ";
		CFBamChainPKey key = schema.getFactoryChain().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamChainBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff readBuff( CFSecAuthorization Authorization,
		CFBamChainPKey PKey )
	{
		final String S_ProcName = "CFBamRamChain.readBuff";
		CFBamChainBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a80f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff lockBuff( CFSecAuthorization Authorization,
		CFBamChainPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamChainBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a80f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamChainBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamChain.readAllBuff";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public CFBamChainBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByIdIdx() ";
		CFBamChainBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
			return( (CFBamChainBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamChainBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByTenantIdx() ";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (CFBamChainBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public CFBamChainBuff[] readBuffByChainTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByChainTableIdx() ";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readDerivedByChainTableIdx( Authorization,
			TenantId,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (CFBamChainBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public CFBamChainBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByDefSchemaIdx() ";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (CFBamChainBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public CFBamChainBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByUNameIdx() ";
		CFBamChainBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
			return( (CFBamChainBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamChainBuff[] readBuffByPrevRelIdx( CFSecAuthorization Authorization,
		long PrevRelationTenantId,
		long PrevRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByPrevRelIdx() ";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readDerivedByPrevRelIdx( Authorization,
			PrevRelationTenantId,
			PrevRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (CFBamChainBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public CFBamChainBuff[] readBuffByNextRelIdx( CFSecAuthorization Authorization,
		long NextRelationTenantId,
		long NextRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readBuffByNextRelIdx() ";
		CFBamChainBuff buff;
		ArrayList<CFBamChainBuff> filteredList = new ArrayList<CFBamChainBuff>();
		CFBamChainBuff[] buffList = readDerivedByNextRelIdx( Authorization,
			NextRelationTenantId,
			NextRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a80f" ) ) {
				filteredList.add( (CFBamChainBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamChainBuff[0] ) );
	}

	public void updateChain( CFSecAuthorization Authorization,
		CFBamChainBuff Buff )
	{
		CFBamChainPKey pkey = schema.getFactoryChain().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamChainBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateChain",
				"Existing record not found",
				"Chain",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateChain",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamChainByTenantIdxKey existingKeyTenantIdx = schema.getFactoryChain().newTenantIdxKey();
		existingKeyTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamChainByTenantIdxKey newKeyTenantIdx = schema.getFactoryChain().newTenantIdxKey();
		newKeyTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamChainByChainTableIdxKey existingKeyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		existingKeyChainTableIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyChainTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamChainByChainTableIdxKey newKeyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		newKeyChainTableIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyChainTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamChainByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamChainByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamChainByUNameIdxKey existingKeyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamChainByUNameIdxKey newKeyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamChainByPrevRelIdxKey existingKeyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		existingKeyPrevRelIdx.setRequiredPrevRelationTenantId( existing.getRequiredPrevRelationTenantId() );
		existingKeyPrevRelIdx.setRequiredPrevRelationId( existing.getRequiredPrevRelationId() );

		CFBamChainByPrevRelIdxKey newKeyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		newKeyPrevRelIdx.setRequiredPrevRelationTenantId( Buff.getRequiredPrevRelationTenantId() );
		newKeyPrevRelIdx.setRequiredPrevRelationId( Buff.getRequiredPrevRelationId() );

		CFBamChainByNextRelIdxKey existingKeyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		existingKeyNextRelIdx.setRequiredNextRelationTenantId( existing.getRequiredNextRelationTenantId() );
		existingKeyNextRelIdx.setRequiredNextRelationId( existing.getRequiredNextRelationId() );

		CFBamChainByNextRelIdxKey newKeyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		newKeyNextRelIdx.setRequiredNextRelationTenantId( Buff.getRequiredNextRelationTenantId() );
		newKeyNextRelIdx.setRequiredNextRelationId( Buff.getRequiredNextRelationId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateChain",
					"ChainUNameIdx",
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
						"updateChain",
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
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateChain",
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPrevRelationTenantId(),
						Buff.getRequiredPrevRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateChain",
						"Lookup",
						"PrevRelation",
						"Relation",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredNextRelationTenantId(),
						Buff.getRequiredNextRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateChain",
						"Lookup",
						"NextRelation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamChainPKey, CFBamChainBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByTenantIdx.get( existingKeyTenantIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByTenantIdx.containsKey( newKeyTenantIdx ) ) {
			subdict = dictByTenantIdx.get( newKeyTenantIdx );
		}
		else {
			subdict = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByTenantIdx.put( newKeyTenantIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByChainTableIdx.get( existingKeyChainTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByChainTableIdx.containsKey( newKeyChainTableIdx ) ) {
			subdict = dictByChainTableIdx.get( newKeyChainTableIdx );
		}
		else {
			subdict = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByChainTableIdx.put( newKeyChainTableIdx, subdict );
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
			subdict = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByPrevRelIdx.get( existingKeyPrevRelIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevRelIdx.containsKey( newKeyPrevRelIdx ) ) {
			subdict = dictByPrevRelIdx.get( newKeyPrevRelIdx );
		}
		else {
			subdict = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByPrevRelIdx.put( newKeyPrevRelIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNextRelIdx.get( existingKeyNextRelIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNextRelIdx.containsKey( newKeyNextRelIdx ) ) {
			subdict = dictByNextRelIdx.get( newKeyNextRelIdx );
		}
		else {
			subdict = new HashMap< CFBamChainPKey, CFBamChainBuff >();
			dictByNextRelIdx.put( newKeyNextRelIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteChain( CFSecAuthorization Authorization,
		CFBamChainBuff Buff )
	{
		final String S_ProcName = "CFBamRamChainTable.deleteChain() ";
		String classCode;
		CFBamChainPKey pkey = schema.getFactoryChain().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamChainBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteChain",
				pkey );
		}
		CFBamChainByTenantIdxKey keyTenantIdx = schema.getFactoryChain().newTenantIdxKey();
		keyTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamChainByChainTableIdxKey keyChainTableIdx = schema.getFactoryChain().newChainTableIdxKey();
		keyChainTableIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyChainTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamChainByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryChain().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamChainByUNameIdxKey keyUNameIdx = schema.getFactoryChain().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamChainByPrevRelIdxKey keyPrevRelIdx = schema.getFactoryChain().newPrevRelIdxKey();
		keyPrevRelIdx.setRequiredPrevRelationTenantId( existing.getRequiredPrevRelationTenantId() );
		keyPrevRelIdx.setRequiredPrevRelationId( existing.getRequiredPrevRelationId() );

		CFBamChainByNextRelIdxKey keyNextRelIdx = schema.getFactoryChain().newNextRelIdxKey();
		keyNextRelIdx.setRequiredNextRelationTenantId( existing.getRequiredNextRelationTenantId() );
		keyNextRelIdx.setRequiredNextRelationId( existing.getRequiredNextRelationId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamChainPKey, CFBamChainBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByTenantIdx.get( keyTenantIdx );
		subdict.remove( pkey );

		subdict = dictByChainTableIdx.get( keyChainTableIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByPrevRelIdx.get( keyPrevRelIdx );
		subdict.remove( pkey );

		subdict = dictByNextRelIdx.get( keyNextRelIdx );
		subdict.remove( pkey );

	}
	public void deleteChainByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamChainPKey key = schema.getFactoryChain().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteChainByIdIdx( Authorization, key );
	}

	public void deleteChainByIdIdx( CFSecAuthorization Authorization,
		CFBamChainPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamChainBuff cur;
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamChainByTenantIdxKey key = schema.getFactoryChain().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteChainByTenantIdx( Authorization, key );
	}

	public void deleteChainByTenantIdx( CFSecAuthorization Authorization,
		CFBamChainByTenantIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByChainTableIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argTableId )
	{
		CFBamChainByChainTableIdxKey key = schema.getFactoryChain().newChainTableIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredTableId( argTableId );
		deleteChainByChainTableIdx( Authorization, key );
	}

	public void deleteChainByChainTableIdx( CFSecAuthorization Authorization,
		CFBamChainByChainTableIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamChainByDefSchemaIdxKey key = schema.getFactoryChain().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteChainByDefSchemaIdx( Authorization, key );
	}

	public void deleteChainByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamChainByDefSchemaIdxKey argKey )
	{
		CFBamChainBuff cur;
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
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argTableId,
		String argName )
	{
		CFBamChainByUNameIdxKey key = schema.getFactoryChain().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteChainByUNameIdx( Authorization, key );
	}

	public void deleteChainByUNameIdx( CFSecAuthorization Authorization,
		CFBamChainByUNameIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByPrevRelIdx( CFSecAuthorization Authorization,
		long argPrevRelationTenantId,
		long argPrevRelationId )
	{
		CFBamChainByPrevRelIdxKey key = schema.getFactoryChain().newPrevRelIdxKey();
		key.setRequiredPrevRelationTenantId( argPrevRelationTenantId );
		key.setRequiredPrevRelationId( argPrevRelationId );
		deleteChainByPrevRelIdx( Authorization, key );
	}

	public void deleteChainByPrevRelIdx( CFSecAuthorization Authorization,
		CFBamChainByPrevRelIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void deleteChainByNextRelIdx( CFSecAuthorization Authorization,
		long argNextRelationTenantId,
		long argNextRelationId )
	{
		CFBamChainByNextRelIdxKey key = schema.getFactoryChain().newNextRelIdxKey();
		key.setRequiredNextRelationTenantId( argNextRelationTenantId );
		key.setRequiredNextRelationId( argNextRelationId );
		deleteChainByNextRelIdx( Authorization, key );
	}

	public void deleteChainByNextRelIdx( CFSecAuthorization Authorization,
		CFBamChainByNextRelIdxKey argKey )
	{
		CFBamChainBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamChainBuff> matchSet = new LinkedList<CFBamChainBuff>();
		Iterator<CFBamChainBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamChainBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteChain( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
