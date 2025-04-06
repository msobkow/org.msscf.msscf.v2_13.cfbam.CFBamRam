
// Description: Java 11 in-memory RAM DbIO implementation for EnumTag.

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
 *	CFBamRamEnumTagTable in-memory RAM DbIO implementation
 *	for EnumTag.
 */
public class CFBamRamEnumTagTable
	implements ICFBamEnumTagTable
{
	private ICFBamSchema schema;
	private Map< CFBamEnumTagPKey,
				CFBamEnumTagBuff > dictByPKey
		= new HashMap< CFBamEnumTagPKey,
				CFBamEnumTagBuff >();
	private Map< CFBamEnumTagByEnumTagTenantIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >> dictByEnumTagTenantIdx
		= new HashMap< CFBamEnumTagByEnumTagTenantIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >>();
	private Map< CFBamEnumTagByEnumIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >> dictByEnumIdx
		= new HashMap< CFBamEnumTagByEnumIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >>();
	private Map< CFBamEnumTagByDefSchemaIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamEnumTagByDefSchemaIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >>();
	private Map< CFBamEnumTagByEnumNameIdxKey,
			CFBamEnumTagBuff > dictByEnumNameIdx
		= new HashMap< CFBamEnumTagByEnumNameIdxKey,
			CFBamEnumTagBuff >();
	private Map< CFBamEnumTagByPrevIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >> dictByPrevIdx
		= new HashMap< CFBamEnumTagByPrevIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >>();
	private Map< CFBamEnumTagByNextIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >> dictByNextIdx
		= new HashMap< CFBamEnumTagByNextIdxKey,
				Map< CFBamEnumTagPKey,
					CFBamEnumTagBuff >>();

	public CFBamRamEnumTagTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createEnumTag( CFSecAuthorization Authorization,
		CFBamEnumTagBuff Buff )
	{
		final String S_ProcName = "createEnumTag";
			CFBamEnumTagBuff tail = null;

			CFBamEnumTagBuff[] siblings = schema.getTableEnumTag().readDerivedByEnumIdx( Authorization,
				Buff.getRequiredTenantId(),
				Buff.getRequiredEnumId() );
			for( int idx = 0; ( tail == null ) && ( idx < siblings.length ); idx ++ ) {
				if( ( siblings[idx].getOptionalNextTenantId() == null )
					&& ( siblings[idx].getOptionalNextId() == null ) )
				{
					tail = siblings[idx];
				}
			}
			if( tail != null ) {
				Buff.setOptionalPrevTenantId( tail.getRequiredTenantId() );
				Buff.setOptionalPrevId( tail.getRequiredId() );
			}
			else {
				Buff.setOptionalPrevTenantId( null );
				Buff.setOptionalPrevId( null );
			}
		
		CFBamEnumTagPKey pkey = schema.getFactoryEnumTag().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( ((CFBamRamTenantTable)schema.getTableTenant()).nextEnumTagIdGen( Authorization,
			Buff.getRequiredTenantId() ) );
		Buff.setRequiredTenantId( pkey.getRequiredTenantId() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamEnumTagByEnumTagTenantIdxKey keyEnumTagTenantIdx = schema.getFactoryEnumTag().newEnumTagTenantIdxKey();
		keyEnumTagTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamEnumTagByEnumIdxKey keyEnumIdx = schema.getFactoryEnumTag().newEnumIdxKey();
		keyEnumIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyEnumIdx.setRequiredEnumId( Buff.getRequiredEnumId() );

		CFBamEnumTagByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryEnumTag().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamEnumTagByEnumNameIdxKey keyEnumNameIdx = schema.getFactoryEnumTag().newEnumNameIdxKey();
		keyEnumNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyEnumNameIdx.setRequiredEnumId( Buff.getRequiredEnumId() );
		keyEnumNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamEnumTagByPrevIdxKey keyPrevIdx = schema.getFactoryEnumTag().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevTenantId( Buff.getOptionalPrevTenantId() );
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamEnumTagByNextIdxKey keyNextIdx = schema.getFactoryEnumTag().newNextIdxKey();
		keyNextIdx.setOptionalNextTenantId( Buff.getOptionalNextTenantId() );
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByEnumNameIdx.containsKey( keyEnumNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"EnumTagEnumNameIdx",
				keyEnumNameIdx );
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
				if( null == schema.getTableEnumDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredEnumId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"EnumDef",
						"EnumDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictEnumTagTenantIdx;
		if( dictByEnumTagTenantIdx.containsKey( keyEnumTagTenantIdx ) ) {
			subdictEnumTagTenantIdx = dictByEnumTagTenantIdx.get( keyEnumTagTenantIdx );
		}
		else {
			subdictEnumTagTenantIdx = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByEnumTagTenantIdx.put( keyEnumTagTenantIdx, subdictEnumTagTenantIdx );
		}
		subdictEnumTagTenantIdx.put( pkey, Buff );

		Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictEnumIdx;
		if( dictByEnumIdx.containsKey( keyEnumIdx ) ) {
			subdictEnumIdx = dictByEnumIdx.get( keyEnumIdx );
		}
		else {
			subdictEnumIdx = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByEnumIdx.put( keyEnumIdx, subdictEnumIdx );
		}
		subdictEnumIdx.put( pkey, Buff );

		Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByEnumNameIdx.put( keyEnumNameIdx, Buff );

		Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			CFBamEnumTagBuff tailEdit = schema.getFactoryEnumTag().newBuff();
			tailEdit.set( (CFBamEnumTagBuff)tail );
				tailEdit.setOptionalNextTenantId( Buff.getRequiredTenantId() );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
			schema.getTableEnumTag().updateEnumTag( Authorization, tailEdit );
		}
	}

	public CFBamEnumTagBuff readDerived( CFSecAuthorization Authorization,
		CFBamEnumTagPKey PKey )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerived";
		CFBamEnumTagPKey key = schema.getFactoryEnumTag().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamEnumTagBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamEnumTagBuff lockDerived( CFSecAuthorization Authorization,
		CFBamEnumTagPKey PKey )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerived";
		CFBamEnumTagPKey key = schema.getFactoryEnumTag().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamEnumTagBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamEnumTagBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamEnumTag.readAllDerived";
		CFBamEnumTagBuff[] retList = new CFBamEnumTagBuff[ dictByPKey.values().size() ];
		Iterator< CFBamEnumTagBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamEnumTagBuff[] readDerivedByEnumTagTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByEnumTagTenantIdx";
		CFBamEnumTagByEnumTagTenantIdxKey key = schema.getFactoryEnumTag().newEnumTagTenantIdxKey();
		key.setRequiredTenantId( TenantId );

		CFBamEnumTagBuff[] recArray;
		if( dictByEnumTagTenantIdx.containsKey( key ) ) {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictEnumTagTenantIdx
				= dictByEnumTagTenantIdx.get( key );
			recArray = new CFBamEnumTagBuff[ subdictEnumTagTenantIdx.size() ];
			Iterator< CFBamEnumTagBuff > iter = subdictEnumTagTenantIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictEnumTagTenantIdx
				= new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByEnumTagTenantIdx.put( key, subdictEnumTagTenantIdx );
			recArray = new CFBamEnumTagBuff[0];
		}
		return( recArray );
	}

	public CFBamEnumTagBuff[] readDerivedByEnumIdx( CFSecAuthorization Authorization,
		long TenantId,
		long EnumId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByEnumIdx";
		CFBamEnumTagByEnumIdxKey key = schema.getFactoryEnumTag().newEnumIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredEnumId( EnumId );

		CFBamEnumTagBuff[] recArray;
		if( dictByEnumIdx.containsKey( key ) ) {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictEnumIdx
				= dictByEnumIdx.get( key );
			recArray = new CFBamEnumTagBuff[ subdictEnumIdx.size() ];
			Iterator< CFBamEnumTagBuff > iter = subdictEnumIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictEnumIdx
				= new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByEnumIdx.put( key, subdictEnumIdx );
			recArray = new CFBamEnumTagBuff[0];
		}
		return( recArray );
	}

	public CFBamEnumTagBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByDefSchemaIdx";
		CFBamEnumTagByDefSchemaIdxKey key = schema.getFactoryEnumTag().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( DefSchemaTenantId );
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamEnumTagBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamEnumTagBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamEnumTagBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictDefSchemaIdx
				= new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamEnumTagBuff[0];
		}
		return( recArray );
	}

	public CFBamEnumTagBuff readDerivedByEnumNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long EnumId,
		String Name )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByEnumNameIdx";
		CFBamEnumTagByEnumNameIdxKey key = schema.getFactoryEnumTag().newEnumNameIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredEnumId( EnumId );
		key.setRequiredName( Name );

		CFBamEnumTagBuff buff;
		if( dictByEnumNameIdx.containsKey( key ) ) {
			buff = dictByEnumNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamEnumTagBuff[] readDerivedByPrevIdx( CFSecAuthorization Authorization,
		Long PrevTenantId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByPrevIdx";
		CFBamEnumTagByPrevIdxKey key = schema.getFactoryEnumTag().newPrevIdxKey();
		key.setOptionalPrevTenantId( PrevTenantId );
		key.setOptionalPrevId( PrevId );

		CFBamEnumTagBuff[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new CFBamEnumTagBuff[ subdictPrevIdx.size() ];
			Iterator< CFBamEnumTagBuff > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictPrevIdx
				= new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new CFBamEnumTagBuff[0];
		}
		return( recArray );
	}

	public CFBamEnumTagBuff[] readDerivedByNextIdx( CFSecAuthorization Authorization,
		Long NextTenantId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByNextIdx";
		CFBamEnumTagByNextIdxKey key = schema.getFactoryEnumTag().newNextIdxKey();
		key.setOptionalNextTenantId( NextTenantId );
		key.setOptionalNextId( NextId );

		CFBamEnumTagBuff[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new CFBamEnumTagBuff[ subdictNextIdx.size() ];
			Iterator< CFBamEnumTagBuff > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdictNextIdx
				= new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new CFBamEnumTagBuff[0];
		}
		return( recArray );
	}

	public CFBamEnumTagBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByIdIdx() ";
		CFBamEnumTagPKey key = schema.getFactoryEnumTag().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamEnumTagBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamEnumTagBuff readBuff( CFSecAuthorization Authorization,
		CFBamEnumTagPKey PKey )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuff";
		CFBamEnumTagBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a81e" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamEnumTagBuff lockBuff( CFSecAuthorization Authorization,
		CFBamEnumTagPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamEnumTagBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a81e" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamEnumTagBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamEnumTag.readAllBuff";
		CFBamEnumTagBuff buff;
		ArrayList<CFBamEnumTagBuff> filteredList = new ArrayList<CFBamEnumTagBuff>();
		CFBamEnumTagBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81e" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamEnumTagBuff[0] ) );
	}

	public CFBamEnumTagBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByIdIdx() ";
		CFBamEnumTagBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a81e" ) ) {
			return( (CFBamEnumTagBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamEnumTagBuff[] readBuffByEnumTagTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByEnumTagTenantIdx() ";
		CFBamEnumTagBuff buff;
		ArrayList<CFBamEnumTagBuff> filteredList = new ArrayList<CFBamEnumTagBuff>();
		CFBamEnumTagBuff[] buffList = readDerivedByEnumTagTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81e" ) ) {
				filteredList.add( (CFBamEnumTagBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamEnumTagBuff[0] ) );
	}

	public CFBamEnumTagBuff[] readBuffByEnumIdx( CFSecAuthorization Authorization,
		long TenantId,
		long EnumId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByEnumIdx() ";
		CFBamEnumTagBuff buff;
		ArrayList<CFBamEnumTagBuff> filteredList = new ArrayList<CFBamEnumTagBuff>();
		CFBamEnumTagBuff[] buffList = readDerivedByEnumIdx( Authorization,
			TenantId,
			EnumId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81e" ) ) {
				filteredList.add( (CFBamEnumTagBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamEnumTagBuff[0] ) );
	}

	public CFBamEnumTagBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByDefSchemaIdx() ";
		CFBamEnumTagBuff buff;
		ArrayList<CFBamEnumTagBuff> filteredList = new ArrayList<CFBamEnumTagBuff>();
		CFBamEnumTagBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81e" ) ) {
				filteredList.add( (CFBamEnumTagBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamEnumTagBuff[0] ) );
	}

	public CFBamEnumTagBuff readBuffByEnumNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long EnumId,
		String Name )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByEnumNameIdx() ";
		CFBamEnumTagBuff buff = readDerivedByEnumNameIdx( Authorization,
			TenantId,
			EnumId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a81e" ) ) {
			return( (CFBamEnumTagBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamEnumTagBuff[] readBuffByPrevIdx( CFSecAuthorization Authorization,
		Long PrevTenantId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByPrevIdx() ";
		CFBamEnumTagBuff buff;
		ArrayList<CFBamEnumTagBuff> filteredList = new ArrayList<CFBamEnumTagBuff>();
		CFBamEnumTagBuff[] buffList = readDerivedByPrevIdx( Authorization,
			PrevTenantId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81e" ) ) {
				filteredList.add( (CFBamEnumTagBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamEnumTagBuff[0] ) );
	}

	public CFBamEnumTagBuff[] readBuffByNextIdx( CFSecAuthorization Authorization,
		Long NextTenantId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByNextIdx() ";
		CFBamEnumTagBuff buff;
		ArrayList<CFBamEnumTagBuff> filteredList = new ArrayList<CFBamEnumTagBuff>();
		CFBamEnumTagBuff[] buffList = readDerivedByNextIdx( Authorization,
			NextTenantId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81e" ) ) {
				filteredList.add( (CFBamEnumTagBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamEnumTagBuff[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamEnumTagBuff moveBuffUp( CFSecAuthorization Authorization,
		long TenantId,
		long Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		CFBamEnumTagBuff grandprev = null;
		CFBamEnumTagBuff prev = null;
		CFBamEnumTagBuff cur = null;
		CFBamEnumTagBuff next = null;

		cur = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, TenantId, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevTenantId() == null )
			&& ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamEnumTagBuff)cur );
		}

		prev = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalPrevTenantId(), cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevTenantId() != null )
			&& ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, prev.getOptionalPrevTenantId(), prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextTenantId() != null )
			&& ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalNextTenantId(), cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		CFBamEnumTagBuff newInstance;
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamEnumTagBuff editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamEnumTagBuff editCur = newInstance;
		editCur.set( cur );

		CFBamEnumTagBuff editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamEnumTagBuff editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext = newInstance;
			editNext.set( next );
		}

		if( editGrandprev != null ) {
			editGrandprev.setOptionalNextTenantId( cur.getRequiredTenantId() );
			editGrandprev.setOptionalNextId( cur.getRequiredId() );
			editCur.setOptionalPrevTenantId( grandprev.getRequiredTenantId() );
			editCur.setOptionalPrevId( grandprev.getRequiredId() );
		}
		else {
			editCur.setOptionalPrevTenantId( null );
			editCur.setOptionalPrevId( null );
		}

			editPrev.setOptionalPrevTenantId( cur.getRequiredTenantId() );
			editPrev.setOptionalPrevId( cur.getRequiredId() );

			editCur.setOptionalNextTenantId( prev.getRequiredTenantId() );
			editCur.setOptionalNextId( prev.getRequiredId() );

		if( next != null ) {
			editPrev.setOptionalNextTenantId( next.getRequiredTenantId() );
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevTenantId( prev.getRequiredTenantId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editPrev.setOptionalNextTenantId( null );
			editPrev.setOptionalNextId( null );
		}

		if( editGrandprev != null ) {
			classCode = editGrandprev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamEnumTagBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamEnumTagBuff moveBuffDown( CFSecAuthorization Authorization,
		long TenantId,
		long Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamEnumTagBuff prev = null;
		CFBamEnumTagBuff cur = null;
		CFBamEnumTagBuff next = null;
		CFBamEnumTagBuff grandnext = null;

		cur = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, TenantId, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextTenantId() == null )
			&& ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamEnumTagBuff)cur );
		}

		next = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalNextTenantId(), cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextTenantId() != null )
			&& ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, next.getOptionalNextTenantId(), next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevTenantId() != null )
			&& ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalPrevTenantId(), cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamEnumTagBuff newInstance;
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamEnumTagBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamEnumTagBuff editNext = newInstance;
		editNext.set( next );

		CFBamEnumTagBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamEnumTagBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev = newInstance;
			editPrev.set( prev );
		}

		if( prev != null ) {
			editPrev.setOptionalNextTenantId( next.getRequiredTenantId() );
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevTenantId( prev.getRequiredTenantId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editNext.setOptionalPrevTenantId( null );
			editNext.setOptionalPrevId( null );
		}

			editCur.setOptionalPrevTenantId( next.getRequiredTenantId() );
			editCur.setOptionalPrevId( next.getRequiredId() );

			editNext.setOptionalNextTenantId( cur.getRequiredTenantId() );
			editNext.setOptionalNextId( cur.getRequiredId() );

		if( editGrandnext != null ) {
			editCur.setOptionalNextTenantId( grandnext.getRequiredTenantId() );
			editCur.setOptionalNextId( grandnext.getRequiredId() );
			editGrandnext.setOptionalPrevTenantId( cur.getRequiredTenantId() );
			editGrandnext.setOptionalPrevId( cur.getRequiredId() );
		}
		else {
			editCur.setOptionalNextTenantId( null );
			editCur.setOptionalNextId( null );
		}

		if( editPrev != null ) {
			classCode = editPrev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamEnumTagBuff)editCur );
	}

	public void updateEnumTag( CFSecAuthorization Authorization,
		CFBamEnumTagBuff Buff )
	{
		CFBamEnumTagPKey pkey = schema.getFactoryEnumTag().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamEnumTagBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateEnumTag",
				"Existing record not found",
				"EnumTag",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateEnumTag",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamEnumTagByEnumTagTenantIdxKey existingKeyEnumTagTenantIdx = schema.getFactoryEnumTag().newEnumTagTenantIdxKey();
		existingKeyEnumTagTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamEnumTagByEnumTagTenantIdxKey newKeyEnumTagTenantIdx = schema.getFactoryEnumTag().newEnumTagTenantIdxKey();
		newKeyEnumTagTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamEnumTagByEnumIdxKey existingKeyEnumIdx = schema.getFactoryEnumTag().newEnumIdxKey();
		existingKeyEnumIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyEnumIdx.setRequiredEnumId( existing.getRequiredEnumId() );

		CFBamEnumTagByEnumIdxKey newKeyEnumIdx = schema.getFactoryEnumTag().newEnumIdxKey();
		newKeyEnumIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyEnumIdx.setRequiredEnumId( Buff.getRequiredEnumId() );

		CFBamEnumTagByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryEnumTag().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamEnumTagByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryEnumTag().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamEnumTagByEnumNameIdxKey existingKeyEnumNameIdx = schema.getFactoryEnumTag().newEnumNameIdxKey();
		existingKeyEnumNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyEnumNameIdx.setRequiredEnumId( existing.getRequiredEnumId() );
		existingKeyEnumNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamEnumTagByEnumNameIdxKey newKeyEnumNameIdx = schema.getFactoryEnumTag().newEnumNameIdxKey();
		newKeyEnumNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyEnumNameIdx.setRequiredEnumId( Buff.getRequiredEnumId() );
		newKeyEnumNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamEnumTagByPrevIdxKey existingKeyPrevIdx = schema.getFactoryEnumTag().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevTenantId( existing.getOptionalPrevTenantId() );
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamEnumTagByPrevIdxKey newKeyPrevIdx = schema.getFactoryEnumTag().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevTenantId( Buff.getOptionalPrevTenantId() );
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamEnumTagByNextIdxKey existingKeyNextIdx = schema.getFactoryEnumTag().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextTenantId( existing.getOptionalNextTenantId() );
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamEnumTagByNextIdxKey newKeyNextIdx = schema.getFactoryEnumTag().newNextIdxKey();
		newKeyNextIdx.setOptionalNextTenantId( Buff.getOptionalNextTenantId() );
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyEnumNameIdx.equals( newKeyEnumNameIdx ) ) {
			if( dictByEnumNameIdx.containsKey( newKeyEnumNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateEnumTag",
					"EnumTagEnumNameIdx",
					newKeyEnumNameIdx );
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
						"updateEnumTag",
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
				if( null == schema.getTableEnumDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredEnumId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateEnumTag",
						"Container",
						"EnumDef",
						"EnumDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByEnumTagTenantIdx.get( existingKeyEnumTagTenantIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByEnumTagTenantIdx.containsKey( newKeyEnumTagTenantIdx ) ) {
			subdict = dictByEnumTagTenantIdx.get( newKeyEnumTagTenantIdx );
		}
		else {
			subdict = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByEnumTagTenantIdx.put( newKeyEnumTagTenantIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByEnumIdx.get( existingKeyEnumIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByEnumIdx.containsKey( newKeyEnumIdx ) ) {
			subdict = dictByEnumIdx.get( newKeyEnumIdx );
		}
		else {
			subdict = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByEnumIdx.put( newKeyEnumIdx, subdict );
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
			subdict = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByEnumNameIdx.remove( existingKeyEnumNameIdx );
		dictByEnumNameIdx.put( newKeyEnumNameIdx, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByPrevIdx.put( newKeyPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNextIdx.get( existingKeyNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNextIdx.containsKey( newKeyNextIdx ) ) {
			subdict = dictByNextIdx.get( newKeyNextIdx );
		}
		else {
			subdict = new HashMap< CFBamEnumTagPKey, CFBamEnumTagBuff >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteEnumTag( CFSecAuthorization Authorization,
		CFBamEnumTagBuff Buff )
	{
		final String S_ProcName = "CFBamRamEnumTagTable.deleteEnumTag() ";
		String classCode;
		CFBamEnumTagPKey pkey = schema.getFactoryEnumTag().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamEnumTagBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteEnumTag",
				pkey );
		}
		long varTenantId = existing.getRequiredTenantId();
		long varEnumId = existing.getRequiredEnumId();
		CFBamEnumDefBuff container = schema.getTableEnumDef().readDerivedByIdIdx( Authorization,
			varTenantId,
			varEnumId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		Long prevTenantId = existing.getOptionalPrevTenantId();
		Long prevId = existing.getOptionalPrevId();
		Long nextTenantId = existing.getOptionalNextTenantId();
		Long nextId = existing.getOptionalNextId();

		CFBamEnumTagBuff prev = null;
		if( ( prevTenantId != null )
			&& ( prevId != null ) )
		{
			prev = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				prevTenantId,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamEnumTagBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				editPrev = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextTenantId( nextTenantId );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamEnumTagBuff next = null;
		if( ( nextTenantId != null )
			&& ( nextId != null ) )
		{
			next = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				nextTenantId,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamEnumTagBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				editNext = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevTenantId( prevTenantId );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamEnumTagByEnumTagTenantIdxKey keyEnumTagTenantIdx = schema.getFactoryEnumTag().newEnumTagTenantIdxKey();
		keyEnumTagTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamEnumTagByEnumIdxKey keyEnumIdx = schema.getFactoryEnumTag().newEnumIdxKey();
		keyEnumIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyEnumIdx.setRequiredEnumId( existing.getRequiredEnumId() );

		CFBamEnumTagByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryEnumTag().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamEnumTagByEnumNameIdxKey keyEnumNameIdx = schema.getFactoryEnumTag().newEnumNameIdxKey();
		keyEnumNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyEnumNameIdx.setRequiredEnumId( existing.getRequiredEnumId() );
		keyEnumNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamEnumTagByPrevIdxKey keyPrevIdx = schema.getFactoryEnumTag().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevTenantId( existing.getOptionalPrevTenantId() );
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamEnumTagByNextIdxKey keyNextIdx = schema.getFactoryEnumTag().newNextIdxKey();
		keyNextIdx.setOptionalNextTenantId( existing.getOptionalNextTenantId() );
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamEnumTagPKey, CFBamEnumTagBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByEnumTagTenantIdx.get( keyEnumTagTenantIdx );
		subdict.remove( pkey );

		subdict = dictByEnumIdx.get( keyEnumIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByEnumNameIdx.remove( keyEnumNameIdx );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

	}
	public void deleteEnumTagByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamEnumTagPKey key = schema.getFactoryEnumTag().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteEnumTagByIdIdx( Authorization, key );
	}

	public void deleteEnumTagByIdIdx( CFSecAuthorization Authorization,
		CFBamEnumTagPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamEnumTagBuff cur;
		LinkedList<CFBamEnumTagBuff> matchSet = new LinkedList<CFBamEnumTagBuff>();
		Iterator<CFBamEnumTagBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamEnumTagBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByEnumTagTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamEnumTagByEnumTagTenantIdxKey key = schema.getFactoryEnumTag().newEnumTagTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteEnumTagByEnumTagTenantIdx( Authorization, key );
	}

	public void deleteEnumTagByEnumTagTenantIdx( CFSecAuthorization Authorization,
		CFBamEnumTagByEnumTagTenantIdxKey argKey )
	{
		CFBamEnumTagBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamEnumTagBuff> matchSet = new LinkedList<CFBamEnumTagBuff>();
		Iterator<CFBamEnumTagBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamEnumTagBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByEnumIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argEnumId )
	{
		CFBamEnumTagByEnumIdxKey key = schema.getFactoryEnumTag().newEnumIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredEnumId( argEnumId );
		deleteEnumTagByEnumIdx( Authorization, key );
	}

	public void deleteEnumTagByEnumIdx( CFSecAuthorization Authorization,
		CFBamEnumTagByEnumIdxKey argKey )
	{
		CFBamEnumTagBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamEnumTagBuff> matchSet = new LinkedList<CFBamEnumTagBuff>();
		Iterator<CFBamEnumTagBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamEnumTagBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamEnumTagByDefSchemaIdxKey key = schema.getFactoryEnumTag().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteEnumTagByDefSchemaIdx( Authorization, key );
	}

	public void deleteEnumTagByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamEnumTagByDefSchemaIdxKey argKey )
	{
		CFBamEnumTagBuff cur;
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
		LinkedList<CFBamEnumTagBuff> matchSet = new LinkedList<CFBamEnumTagBuff>();
		Iterator<CFBamEnumTagBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamEnumTagBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByEnumNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argEnumId,
		String argName )
	{
		CFBamEnumTagByEnumNameIdxKey key = schema.getFactoryEnumTag().newEnumNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredEnumId( argEnumId );
		key.setRequiredName( argName );
		deleteEnumTagByEnumNameIdx( Authorization, key );
	}

	public void deleteEnumTagByEnumNameIdx( CFSecAuthorization Authorization,
		CFBamEnumTagByEnumNameIdxKey argKey )
	{
		CFBamEnumTagBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamEnumTagBuff> matchSet = new LinkedList<CFBamEnumTagBuff>();
		Iterator<CFBamEnumTagBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamEnumTagBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByPrevIdx( CFSecAuthorization Authorization,
		Long argPrevTenantId,
		Long argPrevId )
	{
		CFBamEnumTagByPrevIdxKey key = schema.getFactoryEnumTag().newPrevIdxKey();
		key.setOptionalPrevTenantId( argPrevTenantId );
		key.setOptionalPrevId( argPrevId );
		deleteEnumTagByPrevIdx( Authorization, key );
	}

	public void deleteEnumTagByPrevIdx( CFSecAuthorization Authorization,
		CFBamEnumTagByPrevIdxKey argKey )
	{
		CFBamEnumTagBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamEnumTagBuff> matchSet = new LinkedList<CFBamEnumTagBuff>();
		Iterator<CFBamEnumTagBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamEnumTagBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByNextIdx( CFSecAuthorization Authorization,
		Long argNextTenantId,
		Long argNextId )
	{
		CFBamEnumTagByNextIdxKey key = schema.getFactoryEnumTag().newNextIdxKey();
		key.setOptionalNextTenantId( argNextTenantId );
		key.setOptionalNextId( argNextId );
		deleteEnumTagByNextIdx( Authorization, key );
	}

	public void deleteEnumTagByNextIdx( CFSecAuthorization Authorization,
		CFBamEnumTagByNextIdxKey argKey )
	{
		CFBamEnumTagBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamEnumTagBuff> matchSet = new LinkedList<CFBamEnumTagBuff>();
		Iterator<CFBamEnumTagBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamEnumTagBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
